package com.example.smartbuy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProductRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be 3-50 characters")
    private String product_name;
    private Integer stock;

    private String product_description;

    @NotNull(message = "Price is required")
    private Double product_price;

    private Integer quantity;
    private List<String> imageUrl;

    @NotNull(message = "CategoryId is required")
    private Long categoryId;

    @NotNull(message = "SubCategoryId is required")
    private Long subCategoryId;

    public List<String> getImageUrls() {
        return imageUrl;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrl = imageUrl;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {this.subCategoryId = subCategoryId;}

    public Integer getStock() {return stock;}

    public void setStock(Integer stock) {this.stock = stock;}

}