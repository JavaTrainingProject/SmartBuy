package com.example.smartbuy.controller;

import com.example.smartbuy.ApiResponse;
import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductRequestDto requestDto = mapper.readValue(productJson, ProductRequestDto.class);

            ProductResponseDto response = productService.createProduct(requestDto, images);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        }
    }

    @GetMapping

    public Page<ProductResponseDto> getAllProducts(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return productService.getAllProducts(page, size);

    }

    @GetMapping("/{id}")

    public ApiResponse<ProductResponseDto> getProductById(@PathVariable Long id) {

        ProductResponseDto response = productService.getProductById(id);

        return new ApiResponse<>("Product fetched successfully", true, response);

    }


// ================= UPDATE PRODUCT =================
@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ProductResponseDto> updateProduct(
        @PathVariable Long id,
        @RequestPart("product") ProductRequestDto requestDto,
        @RequestPart(value = "images", required = false) List<MultipartFile> images) {

    ProductResponseDto response = productService.updateProduct(id, requestDto, images);
    return ResponseEntity.ok(response);
}
    // ================= DELETE PRODUCT =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

        String message = productService.deleteProduct(id);
        return ResponseEntity.ok(message);
    }
}
