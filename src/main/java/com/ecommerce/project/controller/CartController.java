package com.ecommerce.project.controller;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositery.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    public CartService cartService;
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
           @PathVariable Long productId,
           @PathVariable Integer quantity
    ){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>>  allCarts(){
        List<CartDTO> carts = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(carts,HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCart(){

        String email = authUtils.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getId();

        CartDTO cartDTO =   cartService.getUserCart(email,cartId);

        return  new ResponseEntity<>(cartDTO,HttpStatus.FOUND);

    }

    @PutMapping("/carts/products/{productId}/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantity(
            @PathVariable Long productId,
            @PathVariable String operation
    ){
        CartDTO  cartDTO = cartService.updateProductQuantityByProductId(productId,
                operation.equalsIgnoreCase("delete") ? -1 :1);

        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);

    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ){
       String status = cartService.deleteProductFromCartById(cartId,productId);
        return new ResponseEntity<String>("deleted successfull",HttpStatus.OK);

    }






}
