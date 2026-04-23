package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.CategoryRequestDto;
import com.example.smartbuy.dtos.CategoryResponseDto;
import com.example.smartbuy.entity.CategoryEntity;

public class CategoryMapper {

    public static CategoryResponseDto toDto(CategoryEntity categoryEntity) {
        if (categoryEntity == null) return null;

        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(categoryEntity.getId());
        dto.setCategoryName(categoryEntity.getCategoryName());
        dto.setCategoryDescription(categoryEntity.getCategoryDescription());

        dto.setCreatedAt(categoryEntity.getCreatedAt());
        dto.setUpdatedAt(categoryEntity.getUpdatedAt());

        return dto;
    }

    public CategoryEntity toEntity(CategoryRequestDto dto) {
        if (dto == null) return null;

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(dto.getCategoryName());
        categoryEntity.setCategoryDescription(dto.getCategoryDescription());
        return categoryEntity;
    }
}
