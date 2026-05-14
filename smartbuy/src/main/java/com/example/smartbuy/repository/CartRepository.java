package com.example.smartbuy.repository;

import com.example.smartbuy.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartProduct, Long> {

    Optional<CartProduct> findByUserIdAndProductId(Long userId, Long productId);

    List<CartProduct> findByUserId(Long userId);
}
