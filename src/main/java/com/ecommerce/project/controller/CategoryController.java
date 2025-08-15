package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(@RequestParam(name = "message",defaultValue = "hello world") String message){
        return new ResponseEntity<>("echoed message:" +message,HttpStatus.OK);

    }


    //Getting all  categories
    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(){
        com.ecommerce.project.payload.CategoryResponse categoryResponse = categoryService.getAllCategories();

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    //Creating  Category
    @RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory( @Valid @RequestBody CategoryDTO categoryDTO){
       CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    //Getting CategoryById

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<Category> searchById(@PathVariable Long categoryId){
        Category category = categoryService.searchCategoryById(categoryId);
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





    //Deleting Category By Id
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){

            CategoryDTO deletedObject = categoryService.deleteCategory(categoryId);
            return  new ResponseEntity<>(deletedObject,HttpStatus.OK);
    }
}
