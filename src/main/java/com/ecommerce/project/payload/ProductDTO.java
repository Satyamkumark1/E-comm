package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private  Long id;
    private  String description;
    private double discount;
    private String image;
    private List<String> images = new ArrayList<>();
    private double price;
    private String productName;
    private Integer quantity;
    private double specialPrice;
}
