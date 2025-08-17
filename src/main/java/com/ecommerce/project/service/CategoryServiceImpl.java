package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositery.CategoryRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepositry categoryRepositry;

    @Autowired
    private ModelMapper modelMapper;


     // Getting Categries  using Sorting with Pageable
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        // Determine sorting direction based on sortOrder parameter
        // If "asc" → ascending, otherwise descending
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable object with page number, size, and sorting details
        Pageable pageableDetails = PageRequest.of(pageNumber,pageSize,sort);

        // Create Pageable object with page number, size, and sorting details
        Page<Category> categoryPage= categoryRepositry.findAll(pageableDetails);

        // Extract the actual list of categories from the page
        List<Category>  categories = categoryPage.getContent();

        // If there are no categories found, throw a custom exception
        if (categories.isEmpty()){
            throw new ApiException("No category created till now");
        }
        //Mapping models
        // Category -> CategoryDTO object usinf ModelMapper
        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        // Creating Object of categoryResponse and then passing the  categoryDTOList .
        CategoryResponse categoryResponse = getCategoryResponse(categoryDTOList, categoryPage); ///total elements

        return categoryResponse; //returning categoryResponse
    }

    private static CategoryResponse getCategoryResponse(List<CategoryDTO> categoryDTOList, Page<Category> categoryPage) {
        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setContent(categoryDTOList); ///List of categories
        categoryResponse.setPageNumber(categoryPage.getNumber());  ///current page number
        categoryResponse.setPageSize(categoryPage.getSize()); /// current page size
        categoryResponse.setTotalPages((long) categoryPage.getTotalPages());  ///total  number of pages
        categoryResponse.setLastPage(categoryPage.isLast()); ///last page
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        return categoryResponse;
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

 //Delete Category by id
    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        //Finding the CategoryById
        Category  category = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId" ,categoryId));


          categoryRepositry.delete(category);
          //  COnvert to DTO and return
        return modelMapper.map(category , CategoryDTO.class);
    }

    //Update Category By Id
    @Override
    public CategoryDTO updateCatgory(CategoryDTO categoryDTO, Long categoryId) {
        //Finding the category by id.
        Category savedCategory = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId)); // returning self made exception
        //Mapping categoryDTO -> category
        Category category = modelMapper.map(categoryDTO, Category.class);

        savedCategory = categoryRepositry.save(category);
        // Convert back to DTO and return
        return modelMapper.map(savedCategory,CategoryDTO.class);

    }

    //Search BY Id
    @Override
    public CategoryDTO searchCategoryById(Long categoryId) {
        // Finding the category By Id from the DB And Converting To DTO and returning or else throwing and exception .
        return categoryRepositry.findById(categoryId)
                .map(category -> modelMapper.map(category, CategoryDTO.class))  
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category " ,categoryId)); //  throwing ResourceNotFoundException that we made
    }


    // Creating categories in bulk
    @Override
    public void createBulkCategories(List<CategoryDTO> categoryDTOList) {

        // Get existing category names
        List<String> existingNames = categoryRepositry.findAll()
                .stream()
                .map(Category::getCategoryName)
                .toList();

        // Filter out duplicates
        List<Category> categoriesToSave = categoryDTOList.stream()
                .filter(dto -> !existingNames.contains(dto.getCategoryName()))
                .map(dto -> modelMapper.map(dto, Category.class))
                .toList();

        // Save them
        categoryRepositry.saveAll(categoriesToSave);
    }


    /// DELETING ALL CATEGORIES
    @Override
    public void deleteAllCategory() {
        categoryRepositry.deleteAll();

    }


}