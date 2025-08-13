package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositry extends JpaRepository<Category , Long> {
}
