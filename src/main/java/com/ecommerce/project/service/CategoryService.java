package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
   CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);


    CategoryDTO updateCatgory(CategoryDTO categoryDTO, Long categoryId);


    CategoryDTO searchCategoryById(Long categoryId);



    List<CategoryDTO> createBulkCategories(@Valid List<CategoryDTO> categoryDTOList);

    List<CategoryDTO> deleteAllCategory();
}
