package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);


    List<ProductDTO> getAllProduct();
}
