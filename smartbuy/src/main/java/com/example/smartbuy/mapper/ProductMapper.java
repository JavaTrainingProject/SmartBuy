package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.entity.ProductEntity;

public class ProductMapper {

        public static ProductResponseDto toDto(ProductEntity entity) {

            ProductResponseDto dto = new ProductResponseDto();

            dto.setId(entity.getId());
            dto.setName(entity.getProductName());   // IMPORTANT
            dto.setDescription(entity.getProductDescription()); // IMPORTANT
            dto.setPrice(entity.getPrice());
            dto.setQuantity(entity.getQuantity());
            dto.setImageUrls(entity.getImageUrl());
            dto.setCreatedAt(entity.getCreatedAt());

            if (entity.getSubCategory() != null) {
                dto.setSubCategoryName(entity.getSubCategory().getSubCategoryName());

                if (entity.getSubCategory().getCategory() != null) {
                    dto.setCategoryName(
                            entity.getSubCategory().getCategory().getCategoryName()
                    );
                }
            }

            return dto;
        }
}
