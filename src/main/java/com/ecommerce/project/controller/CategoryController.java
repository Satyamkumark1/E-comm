package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;




    //Getting all  categories
    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder

    ){
        com.ecommerce.project.payload.CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    //Creating  Category
    @RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory( @Valid @RequestBody CategoryDTO categoryDTO){
       CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/public/bulk", method = RequestMethod.POST)
    public ResponseEntity<List<CategoryDTO>> createBulkCategory(
            @Valid @RequestBody List<CategoryDTO> categoryDTOList) {

        List<CategoryDTO> savedCategoryDTO = categoryService.createBulkCategories(categoryDTOList);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    //Getting CategoryById

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> searchById(@PathVariable Long categoryId){
        CategoryDTO category = categoryService.searchCategoryById(categoryId);
        return ResponseEntity.ok(category);

    }

    //Updating Category By Id

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        CategoryDTO savedCategoryDTO = categoryService.updateCatgory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
    @PostMapping("/bulk")
    public List<CategoryDTO> createCategories(@RequestBody List<CategoryDTO> dtos) {
        return dtos.stream()
                .map(categoryService::createCategory)
                .toList();
    }


     //Delete All Category
    @DeleteMapping("/deleteAll")
    public  List<CategoryDTO> deleteEveryCategory(){
        List<CategoryDTO> deleteAllCategory = categoryService.deleteAllCategory();

        return deleteAllCategory;

    }




    //Deleting Category By Id
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){

            CategoryDTO deletedObject = categoryService.deleteCategory(categoryId);
            return  new ResponseEntity<>(deletedObject,HttpStatus.OK);
    }
}
