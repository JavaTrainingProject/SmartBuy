package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.entity.CartProduct;
import com.example.smartbuy.entity.Order;
import com.example.smartbuy.entity.OrderProduct;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.enums.OrderStatus;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.repository.CartRepository;
import com.example.smartbuy.repository.OrderRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void placeOrder(Long userId) {

        List<CartProduct> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);

        List<OrderProduct> orderProducts = cartItems.stream().map(cart -> {

            ProductEntity product = getProduct(cart.getProductId());

            if (product.getStock() == null || product.getStock() <= 0) {
                throw new RuntimeException("OUT_OF_STOCK: " + product.getProductName());
            }

            if (cart.getQuantity() > product.getStock()) {
                throw new RuntimeException("Quantity exceeds stock");
            }

            product.setStock(product.getStock() - cart.getQuantity());
            productRepository.save(product);

            OrderProduct op = new OrderProduct();
            op.setProductId(cart.getProductId());
            op.setProductName(cart.getProductName());
            op.setPrice(cart.getPrice());
            op.setQuantity(cart.getQuantity());

            op.setOrder(order);

            return op;

        }).toList();

        order.setItems(orderProducts);

        double total = orderProducts.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setTotalAmount(total);

        orderRepository.save(order);

        cartRepository.deleteAll(cartItems);
    }

    @Override
    public ApiResponse<List<Order>> getOrders(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);

        return new ApiResponse<>(
                "SUCCESS",
                "Orders fetched",
                orders
        );
    }

    private ProductEntity getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id", productId)
                );
    }
}