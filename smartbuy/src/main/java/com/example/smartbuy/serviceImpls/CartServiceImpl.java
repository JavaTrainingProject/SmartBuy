package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.AddToCartRequestDto;
import com.example.smartbuy.dtos.CartResponseDto;
import com.example.smartbuy.entity.CartProduct;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.mapper.CartMapper;
import com.example.smartbuy.repository.CartRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.UserRepository;
import com.example.smartbuy.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email))
                .getId();
    }

    @Override
    public CartResponseDto addToCart(AddToCartRequestDto request) {

        Long userId = getCurrentUserId();
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartProduct cart = cartRepository
                .findByUserIdAndProductId(userId, product.getId())
                .orElse(null);

        if (cart == null) {
            cart = new CartProduct();
            cart.setUserId(userId);
            cart.setProductId(product.getId());
            cart.setProductName(product.getProductName());
            cart.setPrice(product.getPrice());
            cart.setQuantity(request.getQuantity());
            cart.setImageUrl(product.getImageUrl());

        } else {
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        }

        CartProduct saved = cartRepository.save(cart); // IMPORTANT

        return CartMapper.toDto(saved);
    }

    @Override
    public List<CartResponseDto> getCart() {
        Long userId = getCurrentUserId();

        return cartRepository.findByUserId(userId)
                .stream()
                .map(CartMapper::toDto)
                .toList();
    }

    @Override
    public void removeItem(Long cartId) {
        CartProduct cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Unauthorized");
        }

        cartRepository.delete(cart);
    }

    @Override
    public void clearCart() {
        Long userId = getCurrentUserId();
        cartRepository.deleteAll(cartRepository.findByUserId(userId));
    }

    @Override
    @Transactional
    public CartResponseDto updateQuantity(Long cartId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Invalid quantity");
        }

        CartProduct cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Unauthorized");
        }

        cart.setQuantity(quantity);


        CartProduct updated = cartRepository.saveAndFlush(cart);

        return CartMapper.toDto(updated);
    }
}