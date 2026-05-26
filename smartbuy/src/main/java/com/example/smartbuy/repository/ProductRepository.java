package com.example.smartbuy.repository;

import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.enums.ProductStatus;
import com.example.smartbuy.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

   Long countByStatus(ProductStatus status);

    List<ProductEntity> findByStatus(ProductStatus status);

    Page<ProductEntity> findBySubCategoryIdAndStatus(Long subCategoryId, Status status, Pageable pageable);

 Page<ProductEntity> findByStatus(
         ProductStatus status,
         Pageable pageable
 );

    Page<ProductEntity> findBySubCategory_Id(Long subCategoryId, Pageable pageable);
 Page<ProductEntity> findBySubCategoryIdAndStatus(
         Long subCategoryId,
         ProductStatus status,
         Pageable pageable
 );

    List<ProductEntity> findByProductName(String productName);

 Optional<ProductEntity> findByProductNameIgnoreCase(String productName);



 List<ProductEntity>
 findByCategory_CategoryName(
         String categoryName
 );




    Page<ProductEntity>
    findByStatusAndSubCategory_StatusAndCategory_Status(
            ProductStatus productStatus,
            Status subCategoryStatus,
            Status categoryStatus,
            Pageable pageable
    );

    Page<ProductEntity>
    findBySubCategoryIdAndStatusAndSubCategory_StatusAndCategory_Status(
            Long subCategoryId,
            ProductStatus productStatus,
            Status subCategoryStatus,
            Status categoryStatus,
            Pageable pageable
    );

    List<ProductEntity>
    findByCategory_CategoryNameAndStatusAndSubCategory_StatusAndCategory_Status(
            String categoryName,
            ProductStatus productStatus,
            Status subCategoryStatus,
            Status categoryStatus
    );

}
