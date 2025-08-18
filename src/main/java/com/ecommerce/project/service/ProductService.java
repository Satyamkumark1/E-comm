package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);


   ProductResponse  getAllProduct();


    ProductDTO getProductById(Long productId);

    ProductResponse searchByCategory(Long categoryId);


    ProductResponse searchProductByKeyword(String keyword);


    ProductDTO updateProductById(Long productId, ProductDTO productDTO);

    ProductDTO deleteProductById(Long productId);
}
