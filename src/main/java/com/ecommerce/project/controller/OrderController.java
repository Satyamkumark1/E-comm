package com.ecommerce.project.controller;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequestDTO;

import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtils authUtils;

    @PostMapping("order/user/payment/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable  String paymentMethod,
                                                  @RequestBody OrderRequestDTO orderRequestDTO){

        // getting the logged in user
      String emailId = authUtils.loggedInEmail();

      OrderDTO order = orderService.placeOrder(
              emailId,
              orderRequestDTO.getAddressId(),
              paymentMethod,
              orderRequestDTO.getPgPaymentId(),
              orderRequestDTO.getPgName(),
              orderRequestDTO.getPgStatus(),
              orderRequestDTO.getPgResponseMessage()
      );
              return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);

    }

    @GetMapping("/order/users")
    public ResponseEntity<java.util.List<OrderDTO>> getUserOrders() {
        String emailId = authUtils.loggedInEmail();
        java.util.List<OrderDTO> orders = orderService.getUserOrders(emailId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
