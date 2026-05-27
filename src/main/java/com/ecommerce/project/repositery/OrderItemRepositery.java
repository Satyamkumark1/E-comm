package com.ecommerce.project.repositery;

import com.ecommerce.project.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepositery extends JpaRepository<OrderItem ,Long> {
    java.util.List<OrderItem> findByOrderId(Long orderId);
}
