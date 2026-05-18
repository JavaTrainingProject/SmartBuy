package com.example.smartbuy.dtos;

import java.util.List;

public class ProductPageRespnseDto {

        private Long totalProducts;

        private List<ProductResponseDto> products;

        public Long getTotalProducts() {
            return totalProducts;
        }

        public void setTotalProducts(Long totalProducts) {
            this.totalProducts = totalProducts;
        }

        public List<ProductResponseDto> getProducts() {
            return products;
        }

        public void setProducts(List<ProductResponseDto> products) {
            this.products = products;
        }
    }