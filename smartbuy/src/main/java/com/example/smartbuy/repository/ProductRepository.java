package com.example.smartbuy.repository;

import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.enums.ProductStatus;
import com.example.smartbuy.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByStatus(ProductStatus status);

    Long countByStatus(ProductStatus status);
}
