package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.entity.*;
import com.example.smartbuy.enums.ProductStatus;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.repository.ProductImageRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.SubCategoryRepository;
import com.example.smartbuy.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

   private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductImageRepository imageRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              SubCategoryRepository subCategoryRepository,
                              ProductImageRepository imageRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.imageRepository = imageRepository;
    }

    // CREATE
    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto, MultipartFile image) {

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        SubCategoryEntity subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        ProductEntity product = new ProductEntity();

        product.setProductName(dto.getProduct_name());
        product.setProductDescription(dto.getProduct_description());
        product.setPrice(dto.getProduct_price());
        product.setQuantity(dto.getQuantity());
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setStatus((ProductStatus.ACTIVE));

        product.setImageUrl(uploadImage(image));

        return mapToResponse(productRepository.save(product));
    }

    // GET ALL
    @Override
    public Page<ProductResponseDto> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    // GET PRODUCT BY ID
    @Override
    public ProductResponseDto getProductById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return mapToResponse(product);
    }

    //  UPDATE
    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto,MultipartFile images) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setProductName(dto.getProduct_name());
        product.setProductDescription(dto.getProduct_description());
        product.setPrice(dto.getProduct_price());
        product.setQuantity(dto.getQuantity());
        product.setUpdatedAt(LocalDateTime.now());
        if (images != null && !images.isEmpty()) {
            String imageUrl = uploadImage(images);
            product.setImageUrl(imageUrl);
        }

        ProductEntity updated = productRepository.save(product);


        return mapToResponse(updated);
    }

    //  DELETE
    @Override
    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }

    //  STATUS
    @Override
    public String updateProductStatus(Long id, String status) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductStatus productStatus;

        try {
            productStatus = ProductStatus.valueOf(status.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status. Use ACTIVE or INACTIVE");
        }

        product.setStatus(productStatus);
        productRepository.save(product);

        return "Status updated successfully";
    }

//upload image
    private String uploadImage(MultipartFile file) {

        if (file == null || file.isEmpty()) return null;

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (Exception e) {
            throw new RuntimeException("Folder creation failed");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try {
            file.transferTo(new File(uploadDir + fileName));
        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
        }

        return "/uploads/" + fileName;
    }

    // IMAGE SAVE
    private List<ProductImage> saveImages(List<MultipartFile> images, ProductEntity product) {

        List<ProductImage> list = new ArrayList<>();

        if (images == null) return list;

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (Exception e) {
            throw new RuntimeException("Folder creation failed");
        }

        for (MultipartFile file : images) {

            if (file.isEmpty()) continue;

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadDir + fileName));
            } catch (Exception e) {
                throw new RuntimeException("File upload failed");
            }

            ProductImage img = new ProductImage();
            img.setImageUrl("/uploads/" + fileName);
            img.setProduct(product);

            list.add(img);
        }

        return imageRepository.saveAll(list);
    }

    //  MAP
    private ProductResponseDto mapToResponse(ProductEntity product) {

        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(product.getId());
        dto.setName(product.getProductName());
        dto.setDescription(product.getProductDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCreatedAt(product.getCreatedAt());

        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getCategoryName());
        }

        if (product.getSubCategory() != null) {
            dto.setSubCategoryName(product.getSubCategory().getSubCategoryName());
        }

        if (product.getImageUrl() != null) {
            dto.setImageUrls(product.getImageUrl());
        }

        return dto;
    }

@Override
public List<ProductResponseDto> getProductsByStatus(String status) {

    ProductStatus productStatus;

    try {
        productStatus = ProductStatus.valueOf(status.toUpperCase());
    } catch (Exception e) {
        throw new IllegalArgumentException("Invalid status. Use ACTIVE or INACTIVE");
    }

    return productRepository.findByStatus(productStatus)
            .stream()
            .map(this::mapToResponse)
            .toList();
}
}
