package com.ecommerce.project.repositery;

import com.ecommerce.project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepositery extends JpaRepository<CartItem ,Long> {

    @Query("SELECT ci from  CartItem  ci where  ci.cart.id = ?1 AND ci.product.id = ?2")
    CartItem findCartItemByProductIdAndCartId(Long id, Long productId);
}
