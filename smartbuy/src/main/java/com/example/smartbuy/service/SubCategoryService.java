package com.example.smartbuy.service;

import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubCategoryService {

    SubCategoryResponseDto createSubCategory(SubCategoryRequestDto dto);

    SubCategoryResponseDto getSubCategoryById(Long id);

    ApiResponse<List<SubCategoryResponseDto>> getAllActiveSubCategories (int page, int size);

    ApiResponse<List<SubCategoryResponseDto>>
    getActiveSubCategoriesByCategory(Long categoryId, int page, int size);

    ApiResponse<String> updateSubCategoryStatus(Long id, Status status);

    SubCategoryResponseDto updateSubCategory(Long id, SubCategoryRequestDto dto);

    ApiResponse<Page<SubCategoryResponseDto>> getAllSubCategories(
            int page,
            int size,
            String sortBy,
            String direction
    );

    ApiResponse<List<SubCategoryResponseDto>> getByStatus(Status status);

    ApiResponse<List<ProductResponseDto>> softDeleteSubCategory(Long id);

    ApiResponse<List<ProductResponseDto>>
    getProductsBySubCategory(
            Long subCategoryId,
            int page,
            int size
    );
}
