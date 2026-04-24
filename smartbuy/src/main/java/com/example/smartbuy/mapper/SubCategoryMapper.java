package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.entity.SubCategoryEntity;
import com.example.smartbuy.enums.Status;

public class SubCategoryMapper {

    // ENTITY -> DTO
    public static SubCategoryResponseDto toDto(SubCategoryEntity sub) {

        SubCategoryResponseDto dto = new SubCategoryResponseDto();

        dto.setId(sub.getId());
        dto.setSubCategoryName(sub.getSubCategoryName());
        dto.setSubCategoryDescription(sub.getSubCategoryDescription());
        dto.setStatus(sub.getStatus());

        if (sub.getCategory() != null) {
            dto.setCategoryId(sub.getCategory().getId());
            dto.setCategoryName(sub.getCategory().getCategoryName());
        }

        return dto;
    }

    // DTO -> ENTITY
    public static SubCategoryEntity toEntity(SubCategoryRequestDto dto) {

        SubCategoryEntity sub = new SubCategoryEntity();

        sub.setSubCategoryName(dto.getSubCategoryName());
        sub.setSubCategoryDescription(dto.getSubCategoryDescription());

        // ⭐ IMPORTANT: default status
        sub.setStatus(Status.ACTIVE);

        return sub;
    }
}