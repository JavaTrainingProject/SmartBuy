package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.OrderRequestDto;
import com.example.smartbuy.dtos.OrderResponseDto;
import com.example.smartbuy.entity.CartProduct;
import com.example.smartbuy.entity.Order;
import com.example.smartbuy.entity.OrderProduct;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.enums.OrderStatus;
import com.example.smartbuy.mapper.OrderMapper;
import com.example.smartbuy.repository.CartRepository;
import com.example.smartbuy.repository.OrderRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.UserRepository;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.OrderService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ))
                .getId();
    }

    @Override
    @Transactional
    public OrderResponseDto placeOrder(
            OrderRequestDto request
    ) {

        if (request.getAddress() == null ||
                request.getAddress().trim().isEmpty()) {

            throw new RuntimeException(
                    "Address is required"
            );
        }

        if (request.getCartItemIds() == null ||
                request.getCartItemIds().isEmpty()) {

            throw new RuntimeException(
                    "Please select cart items"
            );
        }

        Long userId = getCurrentUserId();

        List<CartProduct> cartItems =
                cartRepository.findByIdIn(
                        request.getCartItemIds()
                );

        if (cartItems.isEmpty()) {

            throw new RuntimeException(
                    "Cart items not found"
            );
        }

        for (CartProduct cart : cartItems) {

            if (!cart.getUserId().equals(userId)) {

                throw new RuntimeException(
                        "Unauthorized cart access"
                );
            }
        }

        Order order = new Order();

        order.setUserId(userId);

        order.setAddress(request.getAddress());

        order.setStatus(OrderStatus.CONFIRMED);

        List<OrderProduct> orderProducts =
                new ArrayList<>();

        double totalAmount = 0;

        for (CartProduct cart : cartItems) {

            ProductEntity product =
                    productRepository.findById(
                            cart.getProductId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found"
                            ));

            if (product.getStock() <= 0) {

                throw new RuntimeException(
                        product.getProductName()
                                + " is out of stock"
                );
            }

            if (cart.getQuantity()
                    > product.getStock()) {

                throw new RuntimeException(
                        "Only "
                                + product.getStock()
                                + " items available for "
                                + product.getProductName()
                );
            }

            product.setStock(
                    product.getStock()
                            - cart.getQuantity()
            );

            productRepository.save(product);

            OrderProduct op = new OrderProduct();

            op.setProductId(cart.getProductId());

            op.setProductName(
                    cart.getProductName()
            );

            op.setSubCategoryName(
                    cart.getSubCategoryName()
            );

            op.setProductDescription(
                    cart.getProductDescription()
            );

            op.setPrice(cart.getPrice());

            op.setQuantity(cart.getQuantity());

            op.setImageUrl(cart.getImageUrl());

            op.setOrder(order);

            orderProducts.add(op);

            totalAmount +=
                    cart.getPrice()
                            * cart.getQuantity();
        }

        order.setItems(orderProducts);

        order.setTotalAmount(totalAmount);

        Order savedOrder =
                orderRepository.save(order);

        cartRepository.deleteAll(cartItems);

        return OrderMapper.toDto(savedOrder);
    }

    @Override
    public ApiResponse<List<OrderResponseDto>>
    getOrders() {

        Long userId = getCurrentUserId();

        List<OrderResponseDto> orders =
                orderRepository.findByUserId(userId)
                        .stream()
                        .map(OrderMapper::toDto)
                        .toList();

        return new ApiResponse<>(
                "SUCCESS",
                "Orders fetched successfully",
                orders
        );
    }
}