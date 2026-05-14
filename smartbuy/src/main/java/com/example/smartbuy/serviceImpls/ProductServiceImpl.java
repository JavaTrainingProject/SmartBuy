package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.ProductPageRespnseDto;
import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.entity.*;
import com.example.smartbuy.enums.ProductStatus;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.mapper.ProductMapper;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.repository.ProductImageRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.SubCategoryRepository;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


        private static final Logger log =
                LoggerFactory.getLogger(ProductServiceImpl.class);

        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final SubCategoryRepository subCategoryRepository;

        @Value("${file.upload-dir}")
        private String uploadDir;

        @Value("${app.base-url}")
        private String baseUrl;

        public ProductServiceImpl(ProductRepository productRepository,
                                  CategoryRepository categoryRepository,
                                  SubCategoryRepository subCategoryRepository) {

            this.productRepository = productRepository;
            this.categoryRepository = categoryRepository;
            this.subCategoryRepository = subCategoryRepository;
        }



        @Override
        public ProductResponseDto createProduct(ProductRequestDto dto,
                                                List<MultipartFile> images) {

            CategoryEntity category =
                    categoryRepository.findById(dto.getCategoryId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Category not found"));

            SubCategoryEntity subCategory =
                    subCategoryRepository.findById(dto.getSubCategoryId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("SubCategory not found"));

            ProductEntity product = new ProductEntity();

            product.setProductName(dto.getProduct_name());
            product.setProductDescription(dto.getProduct_description());
            product.setPrice(dto.getProduct_price());
            product.setQuantity(dto.getQuantity());

            product.setCategory(category);
            product.setSubCategory(subCategory);

            product.setStatus(ProductStatus.ACTIVE);

            product.setCreatedAt(LocalDateTime.now());

            String imageUrl = uploadImage(images.get(0));

            product.setImageUrl(imageUrl);

            ProductEntity savedProduct =
                    productRepository.save(product);

            return mapToResponse(savedProduct);
        }


        @Override
        public Page<ProductResponseDto> getAllProducts(int page, int size) {

            return productRepository.findAll(
                            PageRequest.of(page, size)
                    )
                    .map(this::mapToResponse);
        }



        @Override
        public ProductResponseDto getProductById(Long id) {

            ProductEntity product =
                    productRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Product not found"));

            return mapToResponse(product);
        }



        @Override
        public ProductResponseDto updateProduct(Long id,
                                                ProductRequestDto dto,
                                                List<MultipartFile> images) {

            ProductEntity product =
                    productRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Product not found"));

            product.setProductName(dto.getProduct_name());
            product.setProductDescription(dto.getProduct_description());
            product.setPrice(dto.getProduct_price());
            product.setQuantity(dto.getQuantity());

            product.setUpdatedAt(LocalDateTime.now());

            if (images != null && !images.isEmpty()) {

                String imageUrl =
                        uploadImage(images.get(0));

                product.setImageUrl(imageUrl);
            }

            ProductEntity updatedProduct =
                    productRepository.save(product);

            return mapToResponse(updatedProduct);
        }



        @Override
        public String deleteProduct(Long id) {

            if (!productRepository.existsById(id)) {
                throw new ResourceNotFoundException("Product not found");
            }

            productRepository.deleteById(id);

            return "Product deleted successfully";
        }



        @Override
        public String updateProductStatus(Long id, String status) {

            ProductEntity product =
                    productRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Product not found"));

            ProductStatus productStatus;

            try {

                productStatus =
                        ProductStatus.valueOf(status.toUpperCase());

            } catch (Exception e) {

                throw new IllegalArgumentException(
                        "Invalid status. Use ACTIVE or INACTIVE"
                );
            }

            product.setStatus(productStatus);

            productRepository.save(product);

            return "Status updated successfully";
        }



    private String uploadImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        try {

            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalName =
                    file.getOriginalFilename();

            String cleanName =
                    originalName != null
                            ? originalName.replaceAll("\\s+", "_")
                            : "image";

            String fileName =
                    System.currentTimeMillis()
                            + "_"
                            + cleanName;

            File destination =
                    new File(dir, fileName);

            file.transferTo(destination);

            return baseUrl + "/uploads/" + fileName;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Image upload failed", e
            );
        }
    }
        private ProductResponseDto mapToResponse(ProductEntity product) {

            ProductResponseDto dto =
                    new ProductResponseDto();

            dto.setId(product.getId());

            dto.setName(product.getProductName());

            dto.setDescription(product.getProductDescription());

            dto.setPrice(product.getPrice());

            dto.setQuantity(product.getQuantity());

            dto.setCreatedAt(product.getCreatedAt());

            dto.setImageUrl(product.getImageUrl());

            if (product.getCategory() != null) {

                dto.setCategoryName(
                        product.getCategory().getCategoryName()
                );
            }

            if (product.getSubCategory() != null) {

                dto.setSubCategoryName(
                        product.getSubCategory().getSubCategoryName()
                );
            }

            return dto;
        }



        @Override
        public List<ProductResponseDto> getProductsByStatus(String status) {

            ProductStatus productStatus;

            try {

                productStatus =
                        ProductStatus.valueOf(status.toUpperCase());

            } catch (Exception e) {

                throw new IllegalArgumentException(
                        "Invalid status. Use ACTIVE or INACTIVE"
                );
            }

            return productRepository
                    .findByStatus(productStatus)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }



        @Override
        public ApiResponse<ProductPageRespnseDto>
        getProductsBySubCategory(Long subCategoryId,
                                 int page,
                                 int size) {

            Pageable pageable =
                    PageRequest.of(page, size);

            Page<ProductEntity> productPage =
                    productRepository.findBySubCategory_Id(
                            subCategoryId,
                            pageable
                    );

            List<ProductResponseDto> productDtos =
                    productPage.getContent()
                            .stream()
                            .map(this::mapToResponse)
                            .toList();

            ProductPageRespnseDto responseDto =
                    new ProductPageRespnseDto();

            responseDto.setTotalProducts(
                    productPage.getTotalElements()
            );

            responseDto.setProducts(productDtos);

            return new ApiResponse<>(
                    "SUCCESS",
                    "Products fetched successfully",
                    responseDto
            );
        }



        @Override
        public List<ProductEntity> getAllProductByName(String name) {

            log.info("Searching product: {}", name);

            return productRepository.findByProductName(name);
        }






    @Override
    public List<ProductResponseDto>
    getProductsByCategory(
            String categoryName
    ) {

        List<ProductEntity> products =
                productRepository
                        .findByCategory_CategoryName(
                                categoryName
                        );

        return products
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    }