package com.example.smartbuy.entity;

import com.example.smartbuy.enums.ProductStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    private Double price;
    private Integer quantity;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategoryEntity subCategory;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status=ProductStatus.ACTIVE;

    public ProductStatus getStatus() {return status;}

    public void setStatus(ProductStatus status) {this.status = status;}

    public SubCategoryEntity getSubCategory() {return subCategory;}

    public void setSubCategory(SubCategoryEntity subCategory) {this.subCategory = subCategory;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

    public String getProductDescription() {return productDescription;}

    public void setProductDescription(String productDescription) {this.productDescription = productDescription;}

    public Double getPrice() {return price;}

    public void setPrice(Double price) {this.price = price;}

    public Integer getQuantity() {return quantity;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}

    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

    public CategoryEntity getCategory() {return category;}

    public void setCategory(CategoryEntity category) {this.category = category;}

}
