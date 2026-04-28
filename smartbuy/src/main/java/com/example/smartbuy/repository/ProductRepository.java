package com.example.smartbuy.repository;

import com.example.smartbuy.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
