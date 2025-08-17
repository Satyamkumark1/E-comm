package com.ecommerce.project.service;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
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


    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));
        product.setCategoryID(category);

        double specialPrice = product.getPrice() - ((product.getDiscount()* 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        product.setImage("Default");
        Product savedProduct =  productRepositery.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class) ;
    }

    @Override
    public List<ProductDTO> getAllProduct() {

        List<Product> product = productRepositery.findAll();

        List<ProductDTO> productDTOS = product.stream()
                .map(dto-> modelMapper.map(dto, ProductDTO.class))
                .toList();
        return productDTOS;
    }


}
