package com.example.smartbuy.dtos;

public class WishlistResponseDto {

    private Long wishlistId;
    private Long product_id;
    private String product_name;
    private String productDescription;
    private Double price;
    private String image_url;

    public WishlistResponseDto(Long wishlistId, Long product_id, String product_name, String productDescription, Double price, String image_url) {
        this.wishlistId = wishlistId;
        this.product_id = product_id;
        this.product_name = product_name;
        this.productDescription = productDescription;
        this.price = price;
        this.image_url = image_url;
    }

    public WishlistResponseDto() {
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
