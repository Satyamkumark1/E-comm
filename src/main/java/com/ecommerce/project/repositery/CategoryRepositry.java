package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepositry extends JpaRepository<Category , Long> {


    Category findByCategoryName(@NotBlank @Size(min = 5,message = "Category name must contain least 5 characters ") String categoryName);
}
