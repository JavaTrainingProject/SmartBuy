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
        return new ApiResponse<>("SUCCESS", "Added to cart", cartService.addToCart(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<CartResponseDto>> getCart() {
        return new ApiResponse<>("SUCCESS", "Cart fetched", cartService.getCart());
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<String> remove(@PathVariable Long cartId) {
        cartService.removeItem(cartId);
        return new ApiResponse<>("SUCCESS", "Removed", null);
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<String> clear() {
        cartService.clearCart();
        return new ApiResponse<>("SUCCESS", "Cart cleared", null);
    }

    @PutMapping("/{cartId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<CartResponseDto> updateQty(
            @PathVariable Long cartId,
            @RequestBody AddToCartRequestDto request) {

        return new ApiResponse<>("SUCCESS", "Updated",
                cartService.updateQuantity(cartId, request.getQuantity()));
    }
}