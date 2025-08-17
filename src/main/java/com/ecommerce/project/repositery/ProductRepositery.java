package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositery extends JpaRepository<Product,Long> {
}
