package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositery.CartRepository;
import com.ecommerce.project.repositery.CategoryRepositry;
import com.ecommerce.project.repositery.ProductRepositery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class  ProductServiceImp implements ProductService{

     @Autowired
     private  CartRepository cartRepository;

     @Autowired
     private  CartService cartService;

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

    //Get All products
    @Override
    public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageableDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productsPage = productRepositery.findAll(pageableDetails);

        List<Product> product = productsPage.getContent();

        if (product.isEmpty()) {
            throw new ApiException("No Products Exist");
        }

        List<ProductDTO> productDTOS = product.stream()
                .map(dto -> modelMapper.map(dto, ProductDTO.class))
                .toList();

        ProductResponse response = new ProductResponse();
        response.setContent(productDTOS);
        response.setPageNumber(productsPage.getNumber());
        response.setPageSize(productsPage.getSize());
        response.setTotalElements(productsPage.getTotalElements());
        response.setTotalPages(productsPage.getTotalPages());
        response.setLastPage(productsPage.isLast());

        return response;
    }


    //Getting product by productId
    @Override
    public ProductDTO getProductById(Long productId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageableDetails = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> productsPage = productRepositery.findAll(pageableDetails);

        Product product = productRepositery.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));

        return modelMapper.map(product, ProductDTO.class);
    }

    // Searching  product by categoryId
    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("category","categoryId",categoryId));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageableDetails = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> productsPage = productRepositery.findByCategoryOrderByPriceAsc(category,pageableDetails);

         List<Product> products = productsPage.getContent();

         List<ProductDTO> savedProduct = products.stream()
                 .map(dto -> modelMapper.map(dto, ProductDTO.class))
                 .toList();

         ProductResponse productResponse = new ProductResponse();
         productResponse.setContent(savedProduct);
         productResponse.setPageNumber(productsPage.getNumber());
         productResponse.setTotalElements(productsPage.getTotalElements());
         productResponse.setTotalPages(productsPage.getTotalPages());
         productResponse.setLastPage(productsPage.isLast());

        return productResponse;
    }


    //Searching product by keyword
    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product>  pageProducts = productRepositery.findByProductNameContainingIgnoreCase(keyword,pageable);

        List<ProductDTO> productDTO = pageProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setLastPage(pageProducts.isLast());
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());

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

        List<Cart> carts = cartRepository.findCartByProductId(productId);
        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
                    List<ProductDTO> productDTOS = cart.getCartItems().stream()
                            .map( p-> modelMapper.map(p.getProduct(), ProductDTO.class))
                            .collect(Collectors.toList());
                   cartDTO.setProduct(productDTOS);
                   return  cartDTO;
                }).collect(Collectors.toList());
        cartDTOS.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(),productId));



        //returning saved product by mapping to Product Dto
        return modelMapper.map(savedProduct,ProductDTO.class);
    }


    //Deleting product By id
    @Override
    public ProductDTO deleteProductById(Long productId) {
        // Getting the Product from th DB.
        Product productFromDb = productRepositery.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));
        List<Cart> carts = cartRepository.findCartByProductId(productId);
        carts.forEach(cart ->  cartService.deleteProductFromCartById(cart.getId(),productId));
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

    /**
     * Creating Bulk Products By category id
     */

    @Override
    public ProductResponse createBulkProducts(Long categoryId, List<ProductDTO> productDTOS) {
        // 1. Get Category
        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 2. Get existing names (optimize this with custom query if needed)
        List<String> existingNames = productRepositery.findAll()
                .stream()
                .map(Product::getProductName)
                .toList();

        // 3. Filter and map
        List<Product> productsToSave = productDTOS.stream()
                .filter(dto -> !existingNames.contains(dto.getProductName()))
                .map(dto -> {
                    Product product = modelMapper.map(dto, Product.class);
                    product.setCategory(category); // 🔑 link category
                    return product;
                })
                .toList();

        // 4. Save all
        List<Product> savedProducts = productRepositery.saveAll(productsToSave);

        // 5. Convert back to DTOs
        List<ProductDTO> savedProductDTOs = savedProducts.stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .toList();

        // 6. Wrap in response
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(savedProductDTOs);
        return productResponse;
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
