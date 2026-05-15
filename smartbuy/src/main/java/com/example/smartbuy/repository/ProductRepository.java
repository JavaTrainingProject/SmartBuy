package com.example.smartbuy.repository;

import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.enums.ProductStatus;
import com.example.smartbuy.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

   Long countByStatus(ProductStatus status);

    List<ProductEntity> findByStatus(ProductStatus status);

    Page<ProductEntity> findBySubCategoryIdAndStatus(Long subCategoryId, Status status, Pageable pageable);

    List<ProductEntity> findByStatus(Status status);

    Page<ProductEntity> findBySubCategory_Id(Long subCategoryId, Pageable pageable);

    List<ProductEntity> findBySubCategory_Id(Long subCategoryId);
    List<ProductEntity> findByProductName(String productName);



 List<ProductEntity>
 findByCategory_CategoryName(
         String categoryName
 );
}
