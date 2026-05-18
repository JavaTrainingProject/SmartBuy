package com.example.smartbuy.service;

import com.example.smartbuy.dtos.WishlistResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishlistService {

    String addToWishlist(Long product_id, String email);

    String removeFromWishlist(Long wishlistId);

    Page<WishlistResponseDto> getWishlistById(Long user_id, int page, int size);

    boolean isProductInWishlist(Long product_id, String email);
}
