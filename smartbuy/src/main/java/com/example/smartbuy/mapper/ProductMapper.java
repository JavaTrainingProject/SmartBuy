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

            dto.setImageUrl(
                    entity.getImageUrl()
            );

            dto.setCreatedAt(
                    entity.getCreatedAt()
            );

            /* MULTIPLE IMAGES */
            if (entity.getProductImages() != null &&
                    !entity.getProductImages().isEmpty()) {

                dto.setImages(

                        entity.getProductImages()
                                .stream()
                                .map(ProductImage::getImageUrl)
                                .toList()
                );
            }

            /* SUBCATEGORY */
            if (entity.getSubCategory() != null) {

                dto.setSubCategoryName(

                        entity.getSubCategory()
                                .getSubCategoryName()
                );

                /* CATEGORY */
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