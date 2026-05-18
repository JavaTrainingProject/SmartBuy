package com.example.smartbuy.repository;

import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.entity.Wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.smartbuy.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Page<Wishlist> findByUser_IdOrderByIdDesc(Long user_id, Pageable pageable);

    Optional<Wishlist> findByUserAndProduct(UserEntity user, ProductEntity product);

    boolean existsByUserAndProduct(UserEntity user,ProductEntity product);


}
