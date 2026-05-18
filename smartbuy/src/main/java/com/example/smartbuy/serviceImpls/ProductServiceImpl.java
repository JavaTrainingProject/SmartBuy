package com.example.smartbuy.serviceImpls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.smartbuy.dtos.ProductPageRespnseDto;
import com.example.smartbuy.dtos.ProductRequestDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.entity.*;
import com.example.smartbuy.enums.ProductStatus;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.repository.ProductImageRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.SubCategoryRepository;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {



        private final ProductRepository productRepository;

        private final CategoryRepository categoryRepository;

        private final SubCategoryRepository subCategoryRepository;

        private final ProductImageRepository imageRepository;

        private final Cloudinary cloudinary;

        public ProductServiceImpl(
                ProductRepository productRepository,
                CategoryRepository categoryRepository,
                SubCategoryRepository subCategoryRepository,
                ProductImageRepository imageRepository,
                Cloudinary cloudinary
        ) {

            this.productRepository = productRepository;
            this.categoryRepository = categoryRepository;
            this.subCategoryRepository = subCategoryRepository;
            this.imageRepository = imageRepository;
            this.cloudinary = cloudinary;
        }

        @Override
        public ProductResponseDto createProduct(
                ProductRequestDto dto,
                List<MultipartFile> images
        ) {

            CategoryEntity category =
                    categoryRepository.findById(
                                    dto.getCategoryId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Category not found"
                                    )
                            );

            SubCategoryEntity subCategory =
                    subCategoryRepository.findById(
                                    dto.getSubCategoryId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "SubCategory not found"
                                    )
                            );

            ProductEntity product = new ProductEntity();

            product.setProductName(
                    dto.getProduct_name()
            );

            product.setProductDescription(
                    dto.getProduct_description()
            );

            product.setPrice(
                    dto.getProduct_price()
            );

            product.setQuantity(
                    dto.getQuantity()
            );

            product.setStock(
                    dto.getStock()
            );

            product.setCategory(category);

            product.setSubCategory(subCategory);

            product.setStatus(
                    ProductStatus.ACTIVE
            );

            ProductEntity savedProduct =
                    productRepository.save(product);

            if (images != null &&
                    !images.isEmpty()) {

                String thumbnail =
                        uploadImage(images.get(0));

                savedProduct.setImageUrl(
                        thumbnail
                );

                productRepository.save(savedProduct);

                saveImages(images, savedProduct);
            }

            return mapToResponse(savedProduct);
        }

        @Override
        public Page<ProductResponseDto> getAllProducts(
                int page,
                int size
        ) {

            return productRepository
                    .findAll(
                            PageRequest.of(page, size)
                    )
                    .map(this::mapToResponse);
        }

        @Override
        public ProductResponseDto getProductById(
                Long id
        ) {

            ProductEntity product =
                    productRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found"
                                    )
                            );

            return mapToResponse(product);
        }

        @Override
        public ProductResponseDto updateProduct(
                Long id,
                ProductRequestDto dto,
                List<MultipartFile> images
        ) {

            ProductEntity product =
                    productRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found"
                                    )
                            );

            product.setProductName(
                    dto.getProduct_name()
            );

            product.setProductDescription(
                    dto.getProduct_description()
            );

            product.setPrice(
                    dto.getProduct_price()
            );

            product.setQuantity(
                    dto.getQuantity()
            );

            product.setStock(
                    dto.getStock()
            );

            product.setUpdatedAt(
                    LocalDateTime.now()
            );

            if (images != null &&
                    !images.isEmpty()) {

                String thumbnail =
                        uploadImage(images.get(0));

                product.setImageUrl(
                        thumbnail
                );

                saveImages(images, product);
            }

            ProductEntity updatedProduct =
                    productRepository.save(product);

            return mapToResponse(updatedProduct);
        }

        @Override
        public String deleteProduct(
                Long id
        ) {

            if (!productRepository.existsById(id)) {

                throw new ResourceNotFoundException(
                        "Product not found"
                );
            }

            productRepository.deleteById(id);

            return "Product deleted successfully";
        }

        @Override
        public String updateProductStatus(
                Long id,
                String status
        ) {

            ProductEntity product =
                    productRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found"
                                    )
                            );

            ProductStatus productStatus;

            try {

                productStatus =
                        ProductStatus.valueOf(
                                status.toUpperCase()
                        );

            } catch (Exception e) {

                throw new IllegalArgumentException(
                        "Invalid status"
                );
            }

            product.setStatus(productStatus);

            productRepository.save(product);

            return "Status updated successfully";
        }

        @Override
        public List<ProductResponseDto> getProductsByStatus(
                String status
        ) {

            ProductStatus productStatus;

            try {

                productStatus =
                        ProductStatus.valueOf(
                                status.toUpperCase()
                        );

            } catch (Exception e) {

                throw new IllegalArgumentException(
                        "Invalid status"
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
        getProductsBySubCategory(
                Long subCategoryId,
                int page,
                int size
        ) {

            return null;
        }

        @Override
        public List<ProductEntity> getAllProductByName(String productName) {

            return productRepository.findByProductName(productName);
        }

        @Override
        public List<ProductResponseDto>
        getProductsByCategory(String categoryName) {

            return productRepository.findByCategory_CategoryName(categoryName)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        private String uploadImage(MultipartFile file) {

            if (file == null || file.isEmpty()) {

                return null;
            }

            try {

                String contentType =
                        file.getContentType();

                if (contentType == null ||
                        !contentType.startsWith("image/")) {

                    throw new IllegalArgumentException(
                            "Only image files are allowed"
                    );
                }

                Map uploadResult =
                        cloudinary.uploader().upload(
                                file.getBytes(),
                                ObjectUtils.asMap(
                                        "resource_type",
                                        "auto"
                                )
                        );

                return uploadResult
                        .get("secure_url")
                        .toString();

            } catch (Exception e) {

                e.printStackTrace();

                throw new IllegalStateException(
                        e.getMessage()
                );
            }
        }

    private void saveImages(
            List<MultipartFile> images,
            ProductEntity product
    ) {

        if (images == null ||
                images.isEmpty()) {

            return;
        }

        for (int i = 1; i < images.size(); i++) {

            MultipartFile file = images.get(i);

            try {

                Map uploadResult =
                        cloudinary.uploader().upload(
                                file.getBytes(),
                                ObjectUtils.asMap(
                                        "resource_type",
                                        "auto"
                                )
                        );

                String imageUrl =
                        uploadResult
                                .get("secure_url")
                                .toString();

                ProductImage productImage =
                        new ProductImage();

                productImage.setImageUrl(
                        imageUrl
                );

                productImage.setProduct(
                        product
                );

                imageRepository.save(
                        productImage
                );

            } catch (Exception e) {

                e.printStackTrace();

                throw new IllegalStateException(
                        e.getMessage()
                );
            }
        }
    }
        private ProductResponseDto mapToResponse(
                ProductEntity product
        ) {

            ProductResponseDto dto =
                    new ProductResponseDto();

            dto.setId(product.getId());

            dto.setName(
                    product.getProductName()
            );

            dto.setDescription(
                    product.getProductDescription()
            );

            dto.setPrice(
                    product.getPrice()
            );

            dto.setQuantity(
                    product.getQuantity()
            );

            dto.setStock(
                    product.getStock()
            );

            dto.setImageUrls(
                    product.getImageUrl()
            );

            dto.setCreatedAt(
                    product.getCreatedAt()
            );

            if (product.getCategory() != null) {

                dto.setCategoryName(
                        product.getCategory()
                                .getCategoryName()
                );
            }

            if (product.getSubCategory() != null) {

                dto.setSubCategoryName(
                        product.getSubCategory()
                                .getSubCategoryName()
                );
            }

            return dto;
        }
    }


   /* private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductImageRepository imageRepository;
    private final Cloudinary cloudinary;



    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              SubCategoryRepository subCategoryRepository,
                              ProductImageRepository imageRepository,
                              Cloudinary cloudinary) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.imageRepository = imageRepository;
        this.cloudinary=cloudinary;
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto, List<MultipartFile> images) {

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        SubCategoryEntity subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        ProductEntity product = new ProductEntity();

        product.setProductName(dto.getProduct_name());
        product.setProductDescription(dto.getProduct_description());
        product.setPrice(dto.getProduct_price());
        product.setQuantity(dto.getQuantity());
        product.setStock(dto.getStock());

        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setStatus((ProductStatus.ACTIVE));
        if (images != null && !images.isEmpty()) {
            String imageUrl = uploadImage(images.get(0));
            product.setImageUrl(imageUrl);
        }

       // product.setImageUrl(uploadImage(image));
        //return mapToResponse(productRepository.save(product));
        ProductEntity saved=productRepository.save(product);
        return mapToResponse(saved);
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found.."));
        return mapToResponse(product);
    }


//        if (id == null || id <= 0) {
//            throw new IllegalArgumentException("Invalid product ID");
//        }
//
//        ProductEntity product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//
//        return mapToResponse(product);
//    }


    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto, List<MultipartFile> images) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setProductName(dto.getProduct_name());
        product.setProductDescription(dto.getProduct_description());
        product.setPrice(dto.getProduct_price());
        product.setQuantity(dto.getQuantity());
        product.setStock(dto.getStock());
        product.setUpdatedAt(LocalDateTime.now());
        if (images != null && !images.isEmpty()) {
            String imageUrl = uploadImage(images.get(0));
            product.setImageUrl(imageUrl);
        }

        ProductEntity updated = productRepository.save(product);


        return mapToResponse(updated);
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


    private String uploadImage(MultipartFile file) {

        if (file == null || file.isEmpty()) return null;

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            String originalName = file.getOriginalFilename();
            String cleanName = (originalName != null)
                    ? originalName.replaceAll("\\s+", "_")
                    : "image";

            String fileName = System.currentTimeMillis() + "_" + cleanName;

            File destination = new File(dir, fileName);
            file.transferTo(destination);

            return "/uploads/" + fileName;

        } catch (Exception e) {
            throw new IllegalStateException("File upload failed: " + file.getOriginalFilename());
        }
    }


    private List<ProductImage> saveImages(List<MultipartFile> images, ProductEntity product) {

        List<ProductImage> list = new ArrayList<>();

        if (images == null) return list;

        for (MultipartFile file : images) {

            if (file.isEmpty()) continue;

            try {
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String originalName = file.getOriginalFilename();
                String cleanName = (originalName != null)
                        ? originalName.replaceAll("\\s+", "_")
                        : "image";

                String fileName = System.currentTimeMillis() + "_" + cleanName;

                File destination = new File(dir, fileName);
                file.transferTo(destination);

                ProductImage img = new ProductImage();
                img.setImageUrl(baseUrl + "/uploads/" + fileName);
                img.setProduct(product);

                list.add(img);

            } catch (Exception e) {
                throw new IllegalStateException("File upload failed: " + file.getOriginalFilename());
            }
        }

        return imageRepository.saveAll(list);
    }

    private ProductResponseDto mapToResponse(ProductEntity product) {

        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(product.getId());
        dto.setName(product.getProductName());
        dto.setDescription(product.getProductDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setStock(product.getStock());
        dto.setCreatedAt(product.getCreatedAt());

        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getCategoryName());
        }

        if (product.getSubCategory() != null) {
            dto.setSubCategoryName(product.getSubCategory().getSubCategoryName());
        }

        if (product.getImageUrl() != null) {
            dto.setImageUrl(product.getImageUrl());
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

//        return productRepository.findByStatus(productStatus)
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
        return productRepository.findByStatus(productStatus).stream().map(this::mapToResponse).toList();
    }


}
*/