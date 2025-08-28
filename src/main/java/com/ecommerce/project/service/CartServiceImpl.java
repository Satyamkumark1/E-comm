package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositery.CartItemRepositery;
import com.ecommerce.project.repositery.CartRepository;
import com.ecommerce.project.repositery.ProductRepositery;
import com.ecommerce.project.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private  CartRepository cartRepository;

    @Autowired
    AuthUtils authUtil;

    @Autowired
    ProductRepositery productRepositery;

    @Autowired
    CartItemRepositery cartItemRepositery;

    @Autowired
    ModelMapper modelMapper;



    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // Find existing cart or create one
         Cart cart = createCart();

        // Retrieve Product Details
        Product product = productRepositery.findById(productId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Product","ProductId",productId));

        // Perform validation
       CartItem cartItem = cartItemRepositery.findCartItemByProductIdAndCartId(
                cart.getId(),
               productId
        );

       if (cartItem!= null){
           throw new ApiException("Product" + product.getProductName() + "already exist in cart");

       }
       if (product.getQuantity() < quantity) {
           throw new ApiException("Please , make an order of the " + product.getProductName()+
                   "less then or equal to the quantity" + product.getQuantity()+ ".");
       }

        // create cart item
       CartItem newCartItem = new CartItem();
       newCartItem.setProduct(product);
       newCartItem.setProductName(product.getProductName());
       newCartItem.setCart(cart);
       newCartItem.setQuantity(quantity);
       newCartItem.setDiscount(product.getDiscount());
       newCartItem.setProductPrice(product.getSpecialPrice());

        // save cart item
       cartItemRepositery.save(newCartItem);

       cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
       cartRepository.save(cart);

       CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item ->{
                    ProductDTO map = modelMapper.map(item.getProduct(),ProductDTO.class);
                        map.setQuantity(item.getQuantity());
                        return map;
                });

        cartDTO.setProduct(productDTOStream.toList());
        // return updated cart

        return cartDTO ;
    }

    @Override
    public Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart!= null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
         Cart newCart =cartRepository.save(cart);
         return newCart;


    }


}
