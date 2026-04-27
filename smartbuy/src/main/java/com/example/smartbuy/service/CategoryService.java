package com.example.smartbuy.service;

import com.example.smartbuy.ApiResponse;
import com.example.smartbuy.dtos.CategoryRequestDto;
import com.example.smartbuy.dtos.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto dto);

    ApiResponse<List<CategoryResponseDto>> getAllCategories(int page, int size);

    Long getActiveCategoryCount();
}