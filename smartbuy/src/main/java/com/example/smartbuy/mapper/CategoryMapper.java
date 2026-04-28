package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.*;
import com.example.smartbuy.entity.CategoryEntity;
import com.example.smartbuy.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class CategoryMapper {
    
    public static CategoryEntity toEntity(CategoryRequestDto dto) {
        if (dto == null) return null;

        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryName(dto.getCategoryName());
        entity.setCategoryDescription(dto.getCategoryDescription());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.ACTIVE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        return entity;
    }

    public static CategoryResponseDto toDto(CategoryEntity entity) {
        if (entity == null) return null;

        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(entity.getId());
        dto.setCategoryName(entity.getCategoryName());
        dto.setCategoryDescription(entity.getCategoryDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public static void updateEntity(CategoryEntity entity, CategoryRequestDto dto) {
        entity.setCategoryName(dto.getCategoryName());
        entity.setCategoryDescription(dto.getCategoryDescription());

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        entity.setUpdatedAt(LocalDateTime.now());
    }


    public static CategoryWithProductsResponseDto toCategoryWithProductsDto(CategoryEntity category) {

        if (category == null) return null;

        List<ProductResponseDto> products = category.getSubCategories().stream()
                .filter(sub -> sub.getStatus() == Status.ACTIVE)
                .flatMap(sub -> sub.getProduct().stream()) // ⚠ using your existing method
                .map(ProductMapper::toDto)
                .toList();

        if (products.isEmpty()) return null;

        CategoryWithProductsResponseDto dto = new CategoryWithProductsResponseDto();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        dto.setProducts(products);

        return dto;
    }

    public static List<CategoryResponseDto> toDtoList(List<CategoryEntity> entities) {
        return entities.stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    public static List<CategoryWithProductsResponseDto> toCategoryWithProductsList(List<CategoryEntity> categories) {
        return categories.stream()
                .map(CategoryMapper::toCategoryWithProductsDto)
                .filter(Objects::nonNull)
                .toList();
    }
}