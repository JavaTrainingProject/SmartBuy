package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.DashboardStatsDto;
import com.example.smartbuy.enums.ProductStatus;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.SubCategoryRepository;
import com.example.smartbuy.service.DashboardService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private  final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final ProductRepository productRepository;

    public DashboardServiceImpl(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.productRepository = productRepository;
    }

    public DashboardStatsDto getDashboardStats(){

        Long activeCategoriesCount = categoryRepository.countByStatus(Status.ACTIVE);

        Long activeSubCategoriesCount = subCategoryRepository.countByStatus(Status.ACTIVE);

        Long activeProductCount = productRepository.countByStatus(ProductStatus.ACTIVE);

        return new DashboardStatsDto(activeCategoriesCount, activeSubCategoriesCount, activeProductCount);
    }
}
