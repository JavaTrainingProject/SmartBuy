package com.example.smartbuy.controller;

import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.dtos.*;
import com.example.smartbuy.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@RequestBody CategoryRequestDto dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<CategoryResponseDto> getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponseDto> update(@PathVariable Long id,
                                                   @RequestBody CategoryRequestDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> delete(@PathVariable Long id) {
        return categoryService.deleteOrDeactivateCategory(id);
    }


    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<?> getActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return categoryService.getActiveCategories(page, size, sortBy, direction);

    }

    @GetMapping("/active-products")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponse<List<CategoryWithProductsResponseDto>> getActiveWithProducts() {
        return categoryService.getActiveCategoriesWithProducts();
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Long> getActiveCategoryCount() {
        return ResponseEntity.ok(categoryService.getActiveCategoryCount());
    }
}
