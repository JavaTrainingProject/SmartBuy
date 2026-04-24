package com.example.smartbuy.service;

import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;

public interface SubCategoryService {

        SubCategoryResponseDto createSubCategory(SubCategoryRequestDto dto);
    }
