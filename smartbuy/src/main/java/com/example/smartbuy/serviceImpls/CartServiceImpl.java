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

    public CartServiceImpl(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // GET CURRENT USER
    // =========================

    private Long getCurrentUserId() {

        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || auth.getName() == null) {

            throw new RuntimeException(
                    "User not authenticated"
            );
        }

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ))
                .getId();
    }

    // =========================
    // ADD TO CART
    // =========================

    @Override
    public CartResponseDto addToCart(
            AddToCartRequestDto request
    ) {

        // VALIDATIONS

        if (request.getProductId() == null) {

            throw new RuntimeException(
                    "Product id is required"
            );
        }

        if (request.getQuantity() == null ||
                request.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than 0"
            );
        }

        Long userId = getCurrentUserId();

        // GET PRODUCT

        ProductEntity product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        ));

        // STOCK CHECK

        if (product.getStock() <= 0) {

            throw new RuntimeException(
                    product.getProductName()
                            + " is out of stock"
            );
        }

        // CHECK EXISTING CART ITEM

        CartProduct cart = cartRepository
                .findByUserIdAndProductId(
                        userId,
                        request.getProductId()
                )
                .orElse(null);

        int existingQuantity =
                cart == null
                        ? 0
                        : cart.getQuantity();

        int finalQuantity =
                existingQuantity +
                        request.getQuantity();

        // STOCK LIMIT CHECK

        if (finalQuantity > product.getStock()) {

            throw new RuntimeException(
                    "Only "
                            + product.getStock()
                            + " items available in stock"
            );
        }

        // CREATE NEW CART ITEM

        if (cart == null) {

            cart = new CartProduct();

            cart.setUserId(userId);

            cart.setProductId(product.getId());

            cart.setProductName(
                    product.getProductName()
            );

            // =========================
            // FIXED NULL SUBCATEGORY ISSUE
            // =========================

            if (product.getSubCategory() != null) {

                cart.setSubCategoryName(
                        product.getSubCategory()
                                .getSubCategoryName()
                );

            } else {

                cart.setSubCategoryName(
                        "No SubCategory"
                );
            }

            cart.setProductDescription(
                    product.getProductDescription()
            );

            cart.setPrice(product.getPrice());

            cart.setQuantity(
                    request.getQuantity()
            );

            cart.setImageUrl(
                    product.getImageUrl()
            );

        }

        // UPDATE EXISTING CART ITEM
        else {

            cart.setQuantity(finalQuantity);
        }

        CartProduct saved =
                cartRepository.save(cart);

        return CartMapper.toDto(saved);
    }

    // =========================
    // GET CART
    // =========================

    @Override
    public List<CartResponseDto> getCart() {

        Long userId = getCurrentUserId();

        return cartRepository
                .findByUserId(userId)
                .stream()
                .map(CartMapper::toDto)
                .toList();
    }

    // =========================
    // REMOVE ITEM
    // =========================

    @Override
    public void removeItem(Long cartId) {

        CartProduct cart = cartRepository
                .findById(cartId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart item not found"
                        ));

        if (!cart.getUserId()
                .equals(getCurrentUserId())) {

            throw new RuntimeException(
                    "Unauthorized access"
            );
        }

        cartRepository.delete(cart);
    }

    // =========================
    // CLEAR CART
    // =========================

    @Override
    public void clearCart() {

        Long userId = getCurrentUserId();

        List<CartProduct> items =
                cartRepository.findByUserId(userId);

        if (items.isEmpty()) {

            throw new RuntimeException(
                    "Cart is already empty"
            );
        }

        cartRepository.deleteAll(items);
    }

    // =========================
    // UPDATE QUANTITY
    // =========================

    @Override
    @Transactional
    public CartResponseDto updateQuantity(
            Long cartId,
            Integer quantity
    ) {

        if (quantity == null ||
                quantity <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than 0"
            );
        }

        CartProduct cart = cartRepository
                .findById(cartId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart item not found"
                        ));

        if (!cart.getUserId()
                .equals(getCurrentUserId())) {

            throw new RuntimeException(
                    "Unauthorized access"
            );
        }

        ProductEntity product = productRepository
                .findById(cart.getProductId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        ));

        // OUT OF STOCK

        if (product.getStock() <= 0) {

            throw new RuntimeException(
                    product.getProductName()
                            + " is out of stock"
            );
        }

        // STOCK LIMIT

        if (quantity > product.getStock()) {

            throw new RuntimeException(
                    "Only "
                            + product.getStock()
                            + " items available"
            );
        }

        cart.setQuantity(quantity);

        CartProduct updated =
                cartRepository.saveAndFlush(cart);

        return CartMapper.toDto(updated);
    }
}
