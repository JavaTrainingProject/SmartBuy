package com.example.smartbuy.service;

import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto requestDto, MultipartFile images);

    Page<ProductResponseDto> getAllProducts(int page, int size);

    ProductResponseDto getProductById(Long id);

    ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto, MultipartFile images);

    String deleteProduct(Long productId);

    String updateProductStatus(Long id, String status);
    List<ProductResponseDto> getProductsByStatus(String status);

}