package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.UserResponseDto;
import com.example.smartbuy.dtos.WishlistResponseDto;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.entity.Wishlist;

public class WishlistMapper {

    public static WishlistResponseDto toDto(Wishlist wishlist) {

        ProductEntity product = wishlist.getProduct();
        WishlistResponseDto dto = new WishlistResponseDto();

        dto.setWishlistId(wishlist.getId());
        dto.setProduct_id(product.getId());
        dto.setProduct_name(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setPrice(product.getPrice());
        dto.setImage_url(product.getImageUrl());

        return dto;
    }

}
