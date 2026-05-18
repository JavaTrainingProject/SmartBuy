package com.example.smartbuy.service;

import com.example.smartbuy.dtos.AddToCartRequestDto;
import com.example.smartbuy.dtos.CartResponseDto;

import java.util.List;

public interface CartService {

    CartResponseDto addToCart(AddToCartRequestDto request);

    List<CartResponseDto> getCart();

    void removeItem(Long cartId);

    void clearCart();

    CartResponseDto updateQuantity(Long cartId, Integer quantity);
}