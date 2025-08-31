package com.ecommerce.project.service;
import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderItemDTO;
import com.ecommerce.project.repositery.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepositery addressRepositery;

    @Autowired
    private PaymentRepositery paymentRepositery;
    @Autowired
    private OrderRepositery orderRepositery;

    @Autowired
    private OrderItemRepositery orderItemRepositery;

    @Autowired
    private ProductRepositery productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId,
                               String paymentMethod, String pgPaymentId,
                               String pgName, String pgStatus, String pgResponseMessage) {

        // Getting the User Cart by emailId.
        Cart cart = cartRepository.findCartByEmail(emailId);
        //Setting some checks
        if (cart == null){
            throw  new ResourceNotFoundException("cart","email",emailId);
        }
        // Getting the Address by address ID.
        Address address = addressRepositery.findById(addressId)
               .orElseThrow(()-> new ResourceNotFoundException("address","addressId",addressId));

        //Setting th order.
        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!");
        order.setAddress(address);

        //Setting Payment that we get as parameter.
        Payment payment = new Payment(paymentMethod,pgPaymentId,pgStatus,pgName,pgResponseMessage);

        // Setting Order in Payment so its reflects their too.
        payment.setOrder(order);

        // Saving payment in the DB.
        paymentRepositery.save(payment);
        // Setting Payment in Order so its reflects thier too.
        order.setPayment(payment);

        // Saving Order in th DB.
        Order savedOrder = orderRepositery.save(order);

        // Getting the List of CartItems.
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new ApiException("cart is empty");
        }
        //Getting the List od OrderItems so we can add the Products From CartItems.
        List<OrderItem> orderItems = new ArrayList<>();

        //Looping through each CartItems and adding them to the OrderItems List.
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrderProductPrice(cartItem.getProductPrice());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            // adding orderItem to the OrderItems List
            orderItems.add(orderItem);
        }
        // Saving the OrderItem in Db
        orderItemRepositery.saveAll(orderItems);

        //Updating the Stock of products
        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);
            //Saving the changes in Db.
            productRepository.save(product);
            //Deleting the Cart because the Order has been placed
            cartService.deleteProductFromCartById(cart.getId(),item.getProduct().getId());
        });


        // Converting to DTOs for response
        OrderDTO orderDTO = modelMapper.map(savedOrder,OrderDTO.class);
        orderItems.forEach(item ->{
           orderDTO.getOrderItems()
                   .add(modelMapper.map(item, OrderItemDTO.class));
        });
        orderDTO.setAddressId(addressId);




        return orderDTO;
    }
}
