package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);


   ProductResponse  getAllProduct(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);


    ProductDTO getProductById(Long productId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    ProductDTO updateProductById(Long productId, ProductDTO productDTO);

    ProductDTO deleteProductById(Long productId);


    ProductDTO updateProductImageByProductId(Long productId, MultipartFile im) throws IOException;


    ProductResponse createBulkProducts(Long categoryId, @Valid List<ProductDTO> productDTOS);
}
