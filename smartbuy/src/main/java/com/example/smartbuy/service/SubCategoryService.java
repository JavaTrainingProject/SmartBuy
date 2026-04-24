package com.example.smartbuy.service;

import com.example.smartbuy.ApiResponse;
import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.enums.Status;

import java.util.List;

public interface SubCategoryService {

    SubCategoryResponseDto createSubCategory(SubCategoryRequestDto dto);

    SubCategoryResponseDto getSubCategoryById(Long id);

    ApiResponse<List<SubCategoryResponseDto>> getAllActiveSubCategories (int page, int size);

    ApiResponse<List<SubCategoryResponseDto>>
    getActiveSubCategoriesByCategory(Long categoryId, int page, int size);

    ApiResponse<String> updateSubCategoryStatus(Long id, Status status);

    SubCategoryResponseDto updateSubCategory(Long id, SubCategoryRequestDto dto);
    }
