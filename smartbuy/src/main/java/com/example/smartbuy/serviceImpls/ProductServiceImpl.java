package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.entity.CategoryEntity;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.entity.ProductImage;
import com.example.smartbuy.entity.SubCategoryEntity;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.repository.ProductImageRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.SubCategoryRepository;
import com.example.smartbuy.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    //  Constructor Injection (NO @Autowired)
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

    // CREATE PRODUCT
    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto, List<MultipartFile> images) {

        // validations
        if (dto.getCategoryId() == null || dto.getSubCategoryId() == null) {
            throw new RuntimeException("Category or SubCategory ID is missing");
        }

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        SubCategoryEntity subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        if (!subCategory.getCategory().getId().equals(category.getId())) {
            throw new RuntimeException("Invalid category relation");
        }

        ProductEntity product = new ProductEntity();
        product.setProductName(dto.getProduct_name());
        product.setProductDescription(dto.getProduct_description());
        product.setPrice(dto.getProduct_price());
        product.setQuantity(dto.getQuantity());
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setCreatedAt(LocalDateTime.now());

        ProductEntity savedProduct = productRepository.save(product);

        List<ProductImage> savedImages = saveImages(images, savedProduct);
        savedProduct.setImages(savedImages);

        return mapToResponse(savedProduct);
    }

    // GET ALL PRODUCTS
    @Override
    public Page<ProductResponseDto> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductEntity> products = productRepository.findAll(pageable);

        return products.map(this::mapToResponse);
    }

    //  GET BY ID
    @Override
    public ProductResponseDto getProductById(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToResponse(product);
    }

    // UPDATE
    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto, List<MultipartFile> images) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(dto.getProduct_name());
        product.setProductDescription(dto.getProduct_description());
        product.setPrice(dto.getProduct_price());
        product.setQuantity(dto.getQuantity());
        product.setUpdatedAt(LocalDateTime.now());

        ProductEntity updated = productRepository.save(product);

        if (images != null && !images.isEmpty()) {
            List<ProductImage> savedImages = saveImages(images, updated);
            updated.setImages(savedImages);
        }

        return mapToResponse(updated);
    }

    //  DELETE
    @Override
    public String deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        productRepository.deleteById(id);

        return "Product deleted successfully";
    }

    //  IMAGE SAVE METHOD
    private List<ProductImage> saveImages(List<MultipartFile> images, ProductEntity product) {

        List<ProductImage> imageList = new ArrayList<>();

        if (images == null || images.isEmpty()) return imageList;

        String uploadDir = "uploads/";

        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (Exception e) {
            throw new RuntimeException("Could not create upload folder");
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
            img.setImageUrl(uploadDir + fileName);
            img.setProduct(product);

            imageList.add(img);
        }

        return imageRepository.saveAll(imageList);
    }

    private ProductResponseDto mapToResponse(ProductEntity product) {

        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());

        dto.setCategoryName(product.getCategory().getCategoryName());
        dto.setSubCategoryName(product.getSubCategory().getSubCategoryName());

        dto.setCreatedAt(product.getCreatedAt());

        List<String> imageUrls = new ArrayList<>();
        if (product.getImages() != null) {
            for (ProductImage img : product.getImages()) {
                imageUrls.add(img.getImageUrl());
            }
        }

        dto.setImageUrls(imageUrls);

        return dto;
    }

}
