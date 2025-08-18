package com.ecommerce.project.service;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositery.CategoryRepositry;
import com.ecommerce.project.repositery.ProductRepositery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ProductServiceImp implements ProductService{

    @Autowired
    private ProductRepositery productRepositery;
    @Autowired
    private CategoryRepositry categoryRepositry;
    @Autowired
    private ModelMapper modelMapper;


    //Adding product
    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));
        Product product = modelMapper.map(productDTO, Product.class);

        product.setCategory(category);

        double specialPrice = product.getPrice() - ((product.getDiscount()* 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        product.setImage("Default");
        Product savedProduct =  productRepositery.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class) ;
    }

    //Getting all product
    @Override
    public ProductResponse getAllProduct() {

        List<Product> product = productRepositery.findAll();

        List<ProductDTO> productDTOS = product.stream()
                .map(dto-> modelMapper.map(dto, ProductDTO.class))
                .toList();
        ProductResponse response = new ProductResponse();
        response.setContent(productDTOS);
        return response;
    }

    //Getting product by productId
    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepositery.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));

        return modelMapper.map(product, ProductDTO.class);
    }

    // Searching  product by categoryId
    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("category","categoryId",categoryId));

         List<Product> products = productRepositery.findByCategoryOrderByPriceAsc(category);

         List<ProductDTO> savedProduct = products.stream()
                 .map(dto -> modelMapper.map(dto, ProductDTO.class))
                 .toList();

         ProductResponse productResponse = new ProductResponse();
         productResponse.setContent(savedProduct);


        return productResponse;
    }


    //Searching product by keyword
    @Override
    public ProductResponse searchProductByKeyword(String keyword) {

        List<Product> products = productRepositery.findByProductNameContainingIgnoreCase(keyword);

        List<ProductDTO> productDTO = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);

        return productResponse;
    }

    // Updating product by id.
    @Override
    public ProductDTO updateProductById(Long productId, ProductDTO productDTO) {
        // Getting the Product from th DB.
         Product productFromDb = productRepositery.findById(productId)
                 .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));

         Product product = modelMapper.map(productDTO, Product.class);

         //Setting Updated Values
         productFromDb.setProductName(product.getProductName());
         productFromDb.setPrice(product.getPrice());
         productFromDb.setDescription(product.getDescription());
         productFromDb.setDiscount(product.getDiscount());
         productFromDb.setImage(product.getImage());
         productFromDb.setQuantity(product.getQuantity());
         productFromDb.setSpecialPrice(product.getSpecialPrice());

         // Saving the updated value
        Product savedProduct = productRepositery.save(productFromDb);
        //returning saved product by mapping to Product Dto
        return modelMapper.map(savedProduct,ProductDTO.class);
    }


    //Deleting product By id
    @Override
    public ProductDTO deleteProductById(Long productId) {
        // Getting the Product from th DB.
        Product productFromDb = productRepositery.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));
      productRepositery.delete(productFromDb);

        //returning saved product by mapping to Product Dto
        return modelMapper.map(productFromDb,ProductDTO.class);
    }


}
