package com.example.smartbuy.dtos;

import java.util.List;

public class CategoryWithProductsResponseDto {

    private Long id;
    private String categoryName;
    private List<ProductResponseDto> products; // use existing DTO

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductResponseDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponseDto> products) {
        this.products = products;
    }
}
