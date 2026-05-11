package com.example.smartbuy.service;

import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.dtos.CategoryRequestDto;
import com.example.smartbuy.dtos.CategoryResponseDto;
import com.example.smartbuy.dtos.CategoryWithProductsResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto dto);

    ApiResponse<CategoryResponseDto> getCategoryById(Long id);

    ApiResponse<CategoryResponseDto> updateCategory(Long id, CategoryRequestDto dto);

    ApiResponse<String> deleteOrDeactivateCategory(Long id);

    ApiResponse<?> getActiveCategories(int page, int size, String sortBy, String direction);

    ApiResponse<List<CategoryWithProductsResponseDto>> getActiveCategoriesWithProducts();

    ApiResponse<List<CategoryResponseDto>> getAllCategories(int page, int size);

    Long getActiveCategoryCount();

    ApiResponse<Page<CategoryResponseDto>> getAllCategories(
            int page,
            int size,
            String sortBy,
            String direction
    );
}
