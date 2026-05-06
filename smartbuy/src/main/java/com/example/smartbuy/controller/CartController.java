package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.AddToCartRequestDto;
import com.example.smartbuy.dtos.CartResponseDto;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

        private final CartService cartService;

        public CartController(CartService cartService) {
            this.cartService = cartService;
        }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<CartResponseDto> addToCart(@RequestBody AddToCartRequestDto request) {

        return new ApiResponse<>(
                "SUCCESS",
                "Added to cart",
                cartService.addToCart(request)
        );
    }

        @PreAuthorize("hasRole('USER')")
        @GetMapping("/{userId}")
        public ApiResponse<List<CartResponseDto>> getCart(@PathVariable Long userId) {

            return new ApiResponse<>(
                    "SUCCESS",
                    "Cart fetched",
                    cartService.getCart(userId)
            );
        }

        @PreAuthorize("hasRole('USER')")
        @DeleteMapping("/{cartId}")
        public ApiResponse<String> remove(@PathVariable Long cartId) {

            cartService.removeItem(cartId);

            return new ApiResponse<>("SUCCESS", "Removed", null);
        }

        @PreAuthorize("hasRole('USER')")
        @DeleteMapping("/clear/{userId}")
        public ApiResponse<String> clear(@PathVariable Long userId) {

            cartService.clearCart(userId);

            return new ApiResponse<>("SUCCESS", "Cart cleared", null);
        }
    }



