package com.ecommerce.project.controller;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
     private ProductService productService;


    // Adding Products with CategoryId
    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO
                                                , @PathVariable Long categoryId)
    {
        ProductDTO productDTO1 = productService.addProduct(productDTO,categoryId);
        return new ResponseEntity<>(" added", HttpStatus.CREATED);

    }

    // Get All  Products
    @GetMapping("/admin/categories/products")
    public ResponseEntity<ProductResponse> getAllProducts(){

       com.ecommerce.project.payload.ProductResponse productResponse = productService.getAllProduct();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    //Get Product By productById
    @GetMapping("/admin/categories/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId){

        ProductDTO productDTO = productService.getProductById(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }
    //Get product by keyword
    @GetMapping("/products/{keyword}")
    public ResponseEntity<ProductResponse> getByKeyword(@PathVariable String keyword){
        com.ecommerce.project.payload.ProductResponse productResponse = productService.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    //Get Product By category
    @GetMapping("/admin/categories/{categoryId}/products")
    public  ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId){
         ProductResponse  productResponse = productService.searchByCategory(categoryId);
         return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    //Updating Product By productId
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long productId,
                                                        @RequestBody ProductDTO productDTO){
        ProductDTO savedproductDTO = productService.updateProductById(productId,productDTO);
        return  new ResponseEntity<>(productDTO,HttpStatus.OK);
    }


    //Delete product by productId
    @DeleteMapping("admin/products/{productId}")
    public  ResponseEntity<ProductDTO> deleteProductById(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProductById(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }




}
