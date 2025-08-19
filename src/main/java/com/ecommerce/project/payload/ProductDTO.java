package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private  Long id;
    private  String description;
    private double discount;
    private String image;
    private double price;
    private String productName;
    private Integer quantity;
    private double specialPrice;
}
