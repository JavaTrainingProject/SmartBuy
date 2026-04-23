package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.entity.SubCategoryEntity;

public class SubCategoryMapper {
    public static SubCategoryResponseDto toDto(SubCategoryEntity sub){
        SubCategoryResponseDto dto = new SubCategoryResponseDto();
        dto.setId(sub.getId());
        dto.setSubCategoryName(sub.getSubCategoryName());
        dto.setSubCategoryDescription(sub.getSubCategoryDescription());

        if (sub.getCategory() != null) {
            dto.setCategoryId(sub.getCategory().getId());
            dto.setCategoryName(sub.getCategory().getCategoryName());
        }
        return dto;
    }

    public SubCategoryEntity ToEntity (SubCategoryRequestDto dto){
        SubCategoryEntity sub = new SubCategoryEntity();
        sub.setSubCategoryName(dto.getSubCategoryName());
        sub.setSubCategoryDescription(dto.getSubCategoryDescription());
        return sub;
    }
}
