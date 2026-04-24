package com.example.smartbuy.repository;

import com.example.smartbuy.entity.SubCategoryEntity;
import com.example.smartbuy.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    boolean existsBySubCategoryNameIgnoreCase(String subCategoryName);

    Page<SubCategoryEntity> findByStatus(Status status, Pageable pageable);

    Page<SubCategoryEntity> findByCategoryIdAndStatus(
            Long categoryId,
            Status status,
            Pageable pageable
    );
}