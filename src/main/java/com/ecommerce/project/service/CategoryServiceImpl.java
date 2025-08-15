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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    //    private List<Category> categories = new ArrayList<>();

    @Autowired
    private CategoryRepositry categoryRepositry;

    @Autowired

    private ModelMapper modelMapper;


//
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageableDetails = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categoryPage= categoryRepositry.findAll(pageableDetails);

        List<Category>  categories = categoryPage.getContent();
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
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages((long) categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());

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

 //Delete Category by Id
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
        //Finding the category by Id.
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
        return categoryRepositry.findById(categoryId)
                .map(category -> modelMapper.map(category, CategoryDTO.class))  
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category " ,categoryId));
    }


    //Adding Bulk Category
    @Override
    public List<CategoryDTO> createBulkCategories(List<CategoryDTO> categoryDTOList) {
        // Get all existing names
        List<String> existingNames = categoryRepositry.findAll()
                .stream()
                .map(Category::getCategoryName)
                .toList();

        // Filter out duplicates
        List<Category> categoriesToSave = categoryDTOList.stream()
                .filter(dto -> !existingNames.contains(dto.getCategoryName()))
                .map(dto -> modelMapper.map(dto, Category.class))
                .toList();

        List<Category> savedCategories = categoryRepositry.saveAll(categoriesToSave);

        return savedCategories.stream()
                .map(cat -> modelMapper.map(cat, CategoryDTO.class))
                .toList();
    }

    @Override
    public List<CategoryDTO> deleteAllCategory() {
        List<Category> categories = categoryRepositry.findAll();

        List<CategoryDTO> deleted = categories.stream()
                .map(c-> modelMapper.map(c, CategoryDTO.class))
                .toList();
        categoryRepositry.deleteAll();

        return deleted;

    }


}