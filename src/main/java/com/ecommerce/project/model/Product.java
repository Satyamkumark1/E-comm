package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  String description;
    private double discount;
    private String image;
    private double price;
    private String productName;
    private Integer quantity;
    private double specialPrice;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryID;
}
