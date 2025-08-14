package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositery.CategoryRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    //    private List<Category> categories = new ArrayList<>();

    @Autowired
    private CategoryRepositry categoryRepositry;



    @Override
    public List<Category> getAllCategories() {
        return categoryRepositry.findAll();
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepositry.findByCategoryName(category.getCategoryName());
        if (savedCategory != null){
            throw new ApiException("category name" + category + "already exist");
        }
        categoryRepositry.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category  category = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId" ,categoryId));
        categoryRepositry.delete(category);
        return  "Category with Id" + categoryId +" is successfully deleted ";
    }

    @Override
    public Category updateCatgory(Category category, Long categoryId) {
        Category savedCategory = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepositry.save(category);
        return savedCategory;

    }

    @Override
    public Category searchCategoryById(Long categoryId) {
        return categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category " ,categoryId));
    }
}