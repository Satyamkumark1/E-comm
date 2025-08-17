package com.ecommerce.project.controller;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
     private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<String> addProduct(@RequestBody Product product
                                                , @PathVariable Long categoryId)
    {
        ProductDTO productDTO = productService.addProduct(product,categoryId);
        return new ResponseEntity<>(" added", HttpStatus.CREATED);

    }

    @GetMapping("/admin/categories/product")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){

       List<ProductDTO> productDTO = productService.getAllProduct();
        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }


}
