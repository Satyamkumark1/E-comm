package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);


   ProductResponse  getAllProduct();


    ProductDTO getProductById(Long productId);

    ProductResponse searchByCategory(Long categoryId);


    ProductResponse searchProductByKeyword(String keyword);


    ProductDTO updateProductById(Long productId, ProductDTO productDTO);

    ProductDTO deleteProductById(Long productId);


    ProductDTO updateProductImageByProductId(Long productId, MultipartFile im) throws IOException;
}
