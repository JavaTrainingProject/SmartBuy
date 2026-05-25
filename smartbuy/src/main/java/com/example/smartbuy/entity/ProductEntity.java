package com.example.smartbuy.entity;

import com.example.smartbuy.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Lob
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    private Double price;
    private Integer quantity;

    private Integer stock;


    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<ProductImage> productImages = new ArrayList<>();

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    @Column(name = "image_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategoryEntity subCategory;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
