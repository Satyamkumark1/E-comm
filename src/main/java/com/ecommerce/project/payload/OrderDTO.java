package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItems;
    private LocalDate orderDate;
    private PaymentDTO paymentDTO;
    private double totalAmount;
    private String orderStatus;
    private Long addressId;



}
