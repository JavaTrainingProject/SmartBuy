package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.entity.ProductImage;

public class ProductMapper {

        public static ProductResponseDto toDto(
                ProductEntity entity
        ) {

            ProductResponseDto dto =
                    new ProductResponseDto();

            dto.setId(entity.getId());

            dto.setName(
                    entity.getProductName()
            );

            dto.setDescription(
                    entity.getProductDescription()
            );

            dto.setPrice(
                    entity.getPrice()
            );

            dto.setQuantity(
                    entity.getQuantity()
            );

            dto.setStock(
                    entity.getStock()
            );

            dto.setImageUrls(
                    entity.getImageUrl()
            );

            dto.setCreatedAt(
                    entity.getCreatedAt()
            );


            if (
                    entity.getCategory() != null
            ) {

                dto.setCategoryId(
                        entity.getCategory().getId()
                );

                dto.setCategoryName(
                        entity.getCategory().getCategoryName()
                );
            }

            if (entity.getProductImages() != null &&
                    !entity.getProductImages().isEmpty()) {

                dto.setImageUrls(

                        entity.getProductImages()
                                .stream()
                                .map(ProductImage::getImageUrl)
                                .findFirst()
                                .orElse(null)
                );
            }


            if (entity.getSubCategory() != null) {

                dto.setSubCategoryName(

                        entity.getSubCategory()
                                .getSubCategoryName()
                );

                if (entity.getSubCategory()
                        .getCategory() != null) {

                    dto.setCategoryName(

                            entity.getSubCategory()
                                    .getCategory()
                                    .getCategoryName()
                    );
                }
            }

            return dto;
        }
    }