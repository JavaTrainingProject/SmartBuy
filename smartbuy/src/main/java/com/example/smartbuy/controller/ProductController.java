package com.example.smartbuy.controller;

import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Validated
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    //  Constructor Injection

    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    //  CREATE PRODUCT
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) MultipartFile images) throws Exception {

        ProductRequestDto dto =
                objectMapper.readValue(productJson, ProductRequestDto.class);

        ProductResponseDto response =
                productService.createProduct(dto, images);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Product created successfully",  response));
    }


    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductResponseDto> response = productService.getAllProducts(page, size);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Products fetched successfully",  response));
    }


    //  GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {

        ProductResponseDto response = productService.getProductById(id);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Product fetched successfully",  response));
    }


    //  UPDATE
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) MultipartFile images) throws Exception {

        ProductRequestDto dto =
                objectMapper.readValue(productJson, ProductRequestDto.class);

        ProductResponseDto response =
                productService.updateProduct(id, dto, images);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Product updated successfully",  response));
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {

        String message = productService.deleteProduct(id);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS",message,  null));
    }


    //  STATUS UPDATE
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        String response = productService.updateProductStatus(id, status);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Status updated successfully", response)
        );
    }
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductsByStatus(
            @RequestParam String status) {

        List<ProductResponseDto> response =
                productService.getProductsByStatus(status);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Products fetched successfully", response)
        );
    }

}



