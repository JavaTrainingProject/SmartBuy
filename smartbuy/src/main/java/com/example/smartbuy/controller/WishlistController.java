package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.WishlistResponseDto;
import com.example.smartbuy.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add/{product_id}")

    public ResponseEntity<String> addToWishlist(@PathVariable Long product_id, Authentication authentication){
        String email = authentication.getName();

        return ResponseEntity.ok(wishlistService.addToWishlist(product_id,email));
    }

    @DeleteMapping("/remove/{wishlistId}")
    public ResponseEntity<String> removeFromWishlist(
            @PathVariable Long wishlistId
    ){

        return ResponseEntity.ok(
                wishlistService.removeFromWishlist(wishlistId)
        );
    }

    @GetMapping("/{user_id}")

    public ResponseEntity<Page<WishlistResponseDto>> getWishlistById(@PathVariable Long user_id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){


        return ResponseEntity.ok(wishlistService.getWishlistById(user_id,page,size));
    }

    @GetMapping("/check/{product_id}")

    public ResponseEntity<Boolean> checkWishlist(@PathVariable Long product_id, Authentication authentication){
        String email = authentication.getName();
        return  ResponseEntity.ok(wishlistService.isProductInWishlist(product_id, email));
    }
}
