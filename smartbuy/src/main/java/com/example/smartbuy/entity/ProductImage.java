package com.example.smartbuy.entity;

import jakarta.persistence.*;

@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrls;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public Long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getImageUrl() { return imageUrls; }

    public void setImageUrl(String imageUrl) { this.imageUrls = imageUrls; }

    public ProductEntity getProduct() { return product; }

    public void setProduct(ProductEntity product) { this.product = product; }


}