package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.ProductPageRespnseDto;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.service.ProductService;
import com.example.smartbuy.service.SubCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;


    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {

        ProductRequestDto dto =
                objectMapper.readValue(productJson, ProductRequestDto.class);

        ProductResponseDto response =
                productService.createProduct(dto, images);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Product created successfully", response)
        );
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductResponseDto> response =
                productService.getAllProducts(page, size);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS",
                        "Products fetched successfully",
                        response)
        );
    }


    @GetMapping("/{id}")
   @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {

        ProductResponseDto response = productService.getProductById(id);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Product fetched successfully", response));
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws Exception {

        ProductRequestDto dto =
                objectMapper.readValue(productJson, ProductRequestDto.class);

        ProductResponseDto response =
                productService.updateProduct(id, dto, images);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Product updated successfully", response));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {

        String message = productService.deleteProduct(id);

        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", message, null));
    }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        String response = productService.updateProductStatus(id, status);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Status updated successfully", response)
        );
    }


    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductsByStatus(
            @RequestParam String status) {

        List<ProductResponseDto> response =
                productService.getProductsByStatus(status);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Products fetched successfully", response)
        );
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<ApiResponse<ProductPageRespnseDto>> getProductsBySubCategory(
            @PathVariable Long subCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ApiResponse<ProductPageRespnseDto> response =
                productService.getProductsBySubCategory(subCategoryId, page, size);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/category/{categoryName}")

    public ResponseEntity<
            ApiResponse<List<ProductResponseDto>>
            > getProductsByCategory(

            @PathVariable
            String categoryName
    ) {

        List<ProductResponseDto> response =
                productService
                        .getProductsByCategory(
                                categoryName
                        );

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "SUCCESS",

                        "Category products fetched successfully",

                        response
                )
        );
    }


    @GetMapping("/getall-product")
    public ResponseEntity<?> getAllProductByName(@RequestParam String productName) {
          var productList =   productService.getAllProductByName(productName);
          return ResponseEntity.ok(productList);
    }
}



