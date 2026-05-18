package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.CartResponseDto;
import com.example.smartbuy.entity.CartProduct;

public class CartMapper {

    public static CartResponseDto toDto(CartProduct cart) {

        if (cart == null) return null;

        CartResponseDto dto = new CartResponseDto();
        dto.setId(cart.getId());
        dto.setProductId(cart.getProductId());
        dto.setProductName(cart.getProductName());
        dto.setSubCategoryName(cart.getSubCategoryName());
        dto.setPrice(cart.getPrice());
        dto.setQuantity(cart.getQuantity());
        dto.setTotal(cart.getPrice() * cart.getQuantity());
        dto.setImageUrl(cart.getImageUrl());
        dto.setProductDescription(
                cart.getProductDescription()
        );

        return dto;
    }
}