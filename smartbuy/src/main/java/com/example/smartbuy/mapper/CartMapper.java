package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.CartResponseDto;
import com.example.smartbuy.entity.CartProduct;

public class CartMapper {

    public static CartResponseDto toDto(CartProduct item) {

        if (item == null) {
            return null;
        }

        CartResponseDto dto = new CartResponseDto();

        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());

        double total = 0.0;

        if (item.getPrice() != null && item.getQuantity() != null) {
            total = item.getPrice() * item.getQuantity();
        }

        dto.setTotal(total);

        return dto;
    }
}