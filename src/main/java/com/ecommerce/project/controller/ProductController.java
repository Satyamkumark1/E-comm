package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
     private ProductService productService;


    //Adding bulk Products with CategoryId
    @PostMapping("/admin/category/{categoryId}/products")
    public  ResponseEntity<ProductResponse> createBulkCategory(@Valid
                                                      @RequestBody List<ProductDTO> productDTOS,
                                                      @PathVariable Long categoryId
    ){
       ProductResponse productResponse =  productService.createBulkProducts(categoryId,productDTOS);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }

    // Adding Products with CategoryId
    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO
                                                , @PathVariable Long categoryId)
    {
        ProductDTO productDTO1 = productService.addProduct(productDTO,categoryId);
        return new ResponseEntity<>(productDTO1, HttpStatus.CREATED);

    }

    // Get All Products (Admin)
    @GetMapping("/admin/categories/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getAllProduct(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // Get All Products (Public — used by frontend store, seller dashboard, product listing)
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProductsPublic(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getAllProduct(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // Get Single Product by ID (Public)
    @GetMapping("/public/products/{productId}")
    public ResponseEntity<ProductDTO> getProductByIdPublic(@PathVariable Long productId) {
        ProductDTO productDTO = productService.getProductById(productId, 0, 10, "id", "asc");
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    //Get Product By productById
    @GetMapping("/admin/categories/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(
            @RequestParam(name ="pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name ="pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name ="sortBy",defaultValue = AppConstant.SORT_PRODUCTS_BY,required = false) String sortBy,
            @RequestParam(name ="sortOder",defaultValue = AppConstant.SORT_DIR,required = false) String sortOrder,
            @PathVariable Long productId){

        ProductDTO productDTO = productService.getProductById(productId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }
    //Get product by keyword
    @GetMapping("/products/{keyword}")
    public ResponseEntity<ProductResponse> getByKeyword(@PathVariable String keyword,
                                                        @RequestParam(name ="pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
                                                        @RequestParam(name ="pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
                                                        @RequestParam(name ="sortBy",defaultValue = AppConstant.SORT_PRODUCTS_BY,required = false) String sortBy,
                                                        @RequestParam(name ="sortOder",defaultValue = AppConstant.SORT_DIR,required = false) String sortOrder)
    {
        com.ecommerce.project.payload.ProductResponse productResponse = productService.searchProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    //Get Product By category
    @GetMapping("/admin/categories/{categoryId}/products")
    public  ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name ="pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name ="pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name ="sortBy",defaultValue = AppConstant.SORT_PRODUCTS_BY,required = false) String sortBy,
            @RequestParam(name ="sortOder",defaultValue = AppConstant.SORT_DIR,required = false) String sortOrder)

                                                                  {
         ProductResponse  productResponse = productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
         return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    //Updating Product By productId
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long productId,
                                                        @RequestBody ProductDTO productDTO){
        ProductDTO savedproductDTO = productService.updateProductById(productId,productDTO);
        return  new ResponseEntity<>(savedproductDTO,HttpStatus.OK);
    }


    //Delete product by productId
    @DeleteMapping("admin/products/{productId}")
    public  ResponseEntity<ProductDTO> deleteProductById(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProductById(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile im) throws IOException {
        ProductDTO productDTO = productService.updateProductImageByProductId(productId,im);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/images")
    public ResponseEntity<ProductDTO> updateProductImages(@PathVariable Long productId,
                                                          @RequestParam("images") MultipartFile[] ims) throws IOException {
        ProductDTO productDTO = productService.updateProductImagesByProductId(productId, ims);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    // ─── Seller Endpoints ────────────────────────────────────────────────────
    // Mirrors of /admin/** endpoints accessible to ROLE_SELLER and ROLE_ADMIN

    /** Seller: Add a product under a category */
    @PostMapping("/seller/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> sellerAddProduct(@Valid @RequestBody ProductDTO productDTO,
                                                       @PathVariable Long categoryId) {
        ProductDTO saved = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /** Seller: Get all products */
    @GetMapping("/seller/categories/products")
    public ResponseEntity<ProductResponse> sellerGetAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.getAllProduct(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    /** Seller: Get products by category */
    @GetMapping("/seller/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> sellerGetProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    /** Seller: Update a product */
    @PutMapping("/seller/products/{productId}")
    public ResponseEntity<ProductDTO> sellerUpdateProduct(@PathVariable Long productId,
                                                          @RequestBody ProductDTO productDTO) {
        ProductDTO saved = productService.updateProductById(productId, productDTO);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    /** Seller: Delete a product */
    @DeleteMapping("/seller/products/{productId}")
    public ResponseEntity<ProductDTO> sellerDeleteProduct(@PathVariable Long productId) {
        ProductDTO deleted = productService.deleteProductById(productId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

}
