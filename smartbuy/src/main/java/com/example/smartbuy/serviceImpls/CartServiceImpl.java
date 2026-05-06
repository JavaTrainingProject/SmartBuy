package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.AddToCartRequestDto;
import com.example.smartbuy.dtos.CartResponseDto;
import com.example.smartbuy.entity.CartProduct;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.repository.CartRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.UserRepository;
import com.example.smartbuy.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public CartResponseDto addToCart(AddToCartRequestDto request) {

        Long userId = getLoggedInUserId();

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id", request.getProductId()));

        if (product.getStatus() == null ||
                !product.getStatus().name().equals("ACTIVE")) {
            throw new RuntimeException("Product is not ACTIVE");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("Invalid quantity");
        }

        if (product.getStock() == null || product.getStock() < request.getQuantity()) {
            throw new RuntimeException("OUT_OF_STOCK");
        }

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
        } else {
            int newQty = cart.getQuantity() + request.getQuantity();

            if (newQty > product.getStock()) {
                throw new RuntimeException("Quantity exceeds stock");
            }

            cart.setQuantity(newQty);
        }

        CartProduct saved = cartRepository.save(cart);

        return mapToDto(saved);
    }


    @Override
    public List<CartResponseDto> getCart(Long userId) {

        return cartRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public void removeItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void clearCart(Long userId) {
        List<CartProduct> items = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(items);
    }


    private CartResponseDto mapToDto(CartProduct cart) {

        CartResponseDto dto = new CartResponseDto();
        dto.setProductId(cart.getProductId());
        dto.setProductName(cart.getProductName());
        dto.setPrice(cart.getPrice());
        dto.setQuantity(cart.getQuantity());
        dto.setTotal(cart.getPrice() * cart.getQuantity());

        return dto;
    }

    private Long getLoggedInUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String username = authentication.getName();

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();}
}