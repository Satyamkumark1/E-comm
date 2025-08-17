package com.ecommerce.project.controller;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
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


    // Adding Products with CategoryId
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<String> addProduct(@RequestBody Product product
                                                , @PathVariable Long categoryId)
    {
        ProductDTO productDTO = productService.addProduct(product,categoryId);
        return new ResponseEntity<>(" added", HttpStatus.CREATED);

    }

    //
    @GetMapping("/admin/categories/product")
    public ResponseEntity<ProductResponse> getAllProducts(){

       com.ecommerce.project.payload.ProductResponse productResponse = productService.getAllProduct();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/admin/categories/product/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId){

        ProductDTO productDTO = productService.getProductById(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }


}
