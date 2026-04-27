package com.example.smartbuy.controller;

import com.example.smartbuy.ApiResponse;
import com.example.smartbuy.dtos.CategoryRequestDto;
import com.example.smartbuy.dtos.CategoryResponseDto;
import com.example.smartbuy.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto dto) {
        return new ResponseEntity<>(categoryService.createCategory(dto), HttpStatus.CREATED);
    }

    @GetMapping("/getCategories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                categoryService.getAllCategories(page, size)
        );
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getActiveCategoryCount() {
        return ResponseEntity.ok(categoryService.getActiveCategoryCount());
    }
}
