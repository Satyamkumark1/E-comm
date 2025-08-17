package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositery extends JpaRepository<Product,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);
}
