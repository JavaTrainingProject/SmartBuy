package com.example.smartbuy.service;

import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProductService {

    Page<ProductResponseDto> getAllProducts(int page, int size);

    ProductResponseDto createProduct(ProductRequestDto requestDto, List<MultipartFile> images);

    ProductResponseDto getProductById(Long id);

    ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto, List<MultipartFile> images);

    String deleteProduct(Long productId);

}

