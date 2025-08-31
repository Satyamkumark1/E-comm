package com.ecommerce.project.model;


import com.ecommerce.project.payload.PaymentDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderItem> orderItem = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "address_id")
    private  Address address;

    @OneToOne
    @JoinColumn(name = "payment")
    private Payment payment;

    private LocalDate orderDate;
    private double totalAmount;
    private String orderStatus;


}
