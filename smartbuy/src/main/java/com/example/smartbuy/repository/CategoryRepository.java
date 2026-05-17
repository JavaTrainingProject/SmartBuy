package com.example.smartbuy.repository;

import com.example.smartbuy.enums.Status;
import com.example.smartbuy.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByCategoryNameIgnoreCase(String categoryName);

    List<CategoryEntity> findByStatus(Status status);

    Page<CategoryEntity> findByStatus(Status status, Pageable pageable);

    Long countByStatus(Status status);
}
