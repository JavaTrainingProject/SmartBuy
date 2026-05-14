package com.example.smartbuy.repository;

import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.entity.SubCategoryEntity;
import com.example.smartbuy.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    boolean existsBySubCategoryNameIgnoreCase(String subCategoryName);

    boolean existsBySubCategoryNameIgnoreCaseAndIdNot(
            String subCategoryName,
            Long id
    );

    Page<SubCategoryEntity> findByStatus(Status status, Pageable pageable);

    List<SubCategoryEntity> findByStatus(Status status);

    Long countByStatus(Status status);

    Page<SubCategoryEntity> findByCategoryIdAndStatus(Long categoryId, Status status, Pageable pageable);


}