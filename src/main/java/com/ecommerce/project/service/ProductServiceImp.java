package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


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
        Product product1 = modelMapper.map(productDTO, Product.class);

      Product productFromDb=  productRepositery.findProductByProductName(product1.getProductName());
      if (productFromDb !=null){
          throw  new ApiException("product name " + product1.getProductName() + " already exist");
      }


        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));
        Product product = modelMapper.map(productDTO, Product.class);

        product.setCategory(category);

        double specialPrice = product.getPrice() - ((product.getDiscount()* 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        product.setImage("Default");
        product.setProductName(product.getProductName());
        Product savedProduct =  productRepositery.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class) ;
    }

    //Getting all product
    @Override
    public ProductResponse getAllProduct() {
        //check product size is 0.

        List<Product> product = productRepositery.findAll();

        List<ProductDTO> productDTOS = product.stream()
                .map(dto-> modelMapper.map(dto, ProductDTO.class))
                .toList();
        if (product.isEmpty()){
            throw  new ApiException("No Products Exist");
        }
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

    @Override
    public ProductDTO updateProductImageByProductId(Long productId, MultipartFile im) throws IOException {
       Product productFromDb = productRepositery.findById(productId)
               .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));
       //Upload image to server
        //get the file name of uploaded image
        String path = "images/";
        String filename = uploadImage(path,im);

        //Updating the new file name to the product
        productFromDb.setImage(filename);

        //Save update
       Product savedProduct = productRepositery.save(productFromDb);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        // File names of current / original file
        String originalFileName = file.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        // mat.jpg --> 1234 --> 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        // Check if path exist and create
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();

        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // returning file name
        return fileName;
    }


}
