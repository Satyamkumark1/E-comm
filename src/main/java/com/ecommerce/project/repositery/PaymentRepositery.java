package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepositery extends JpaRepository<Payment,Long> {
}

