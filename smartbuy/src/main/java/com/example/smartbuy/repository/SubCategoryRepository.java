package com.example.smartbuy.repository;

import com.example.smartbuy.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    boolean existsBySubCategoryNameIgnoreCase(String subCategoryName);
}