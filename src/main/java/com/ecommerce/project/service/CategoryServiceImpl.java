package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositery.CategoryRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    //    private List<Category> categories = new ArrayList<>();

    @Autowired
    private CategoryRepositry categoryRepositry;

    @Autowired

    private ModelMapper modelMapper;



    @Override
    public CategoryResponse getAllCategories() {
        List<Category>  categories = categoryRepositry.findAll();
        if (categories.isEmpty()){
            throw new ApiException("No category created till now");
        }
        //Mapping models
        // Category -> CategoryDTO
        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        // Creating Object of categoryResponse and then passing the  categoryDTOList .
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOList);
        return categoryResponse; //returning categoryResponse
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Convert DTO to Entity
        Category category = modelMapper.map(categoryDTO, Category.class);

        // Check if category name already exists
        Category categoryFromDb = categoryRepositry.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null) {
            throw new ApiException("Category name '" + category.getCategoryName() + "' already exists");
        }

        // Save category
        Category savedCategory = categoryRepositry.save(category);

        // Convert back to DTO and return
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        //Finding the CategoryById
        Category  category = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId" ,categoryId));


          categoryRepositry.delete(category);
          //  COnvert to DTO and return
        return modelMapper.map(category , CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCatgory(CategoryDTO categoryDTO, Long categoryId) {
        //Finding the category by Id.
        Category savedCategory = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId)); // returning self made exception
        //Mapping categoryDTO -> category
        Category category = modelMapper.map(categoryDTO, Category.class);

        savedCategory = categoryRepositry.save(category);
        // Convert back to DTO and return
        return modelMapper.map(savedCategory,CategoryDTO.class);

    }

    @Override
    public Category searchCategoryById(Long categoryId) {
        return categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category " ,categoryId));
    }
}