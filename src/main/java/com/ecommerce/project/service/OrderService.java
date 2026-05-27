package com.ecommerce.project.service;

import com.ecommerce.project.payload.OrderDTO;
import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgPaymentId, String pgName, String pgStatus, String pgResponseMessage);
    List<OrderDTO> getUserOrders(String emailId);
}
