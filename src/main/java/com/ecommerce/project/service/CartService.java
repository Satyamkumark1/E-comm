package com.ecommerce.project.service;


import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {


    CartDTO addProductToCart(Long productId, Integer quantity);
    Cart createCart();

    List<CartDTO> getAllCarts();


    CartDTO getUserCart(String email, Long cartId);


    @Transactional
    CartDTO updateProductQuantityByProductId(Long productId, Integer quatity);


    String deleteProductFromCartById(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}
