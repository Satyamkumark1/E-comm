package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart , Long> {
    @Query("SELECT c from Cart  c WHERE  c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("select  c from Cart  c where c.user.email =?1 and  c.id = ?2")
    Cart findCartByEmailAndCartId(String email, Long cartId);

    @Transactional
    @Modifying
    @Query("DELETE from CartItem ci where  ci.cart.id =?1 and ci.product.id =?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);

    @Query("SELECT c  from Cart c JOIN  fetch  c.cartItems ci JOIN  fetch ci.product p where  p.id =?1")
    List<Cart> findCartByProductId(Long productId);
}
