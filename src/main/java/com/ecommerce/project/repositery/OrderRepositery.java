package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositery extends JpaRepository<Order,Long> {
}
