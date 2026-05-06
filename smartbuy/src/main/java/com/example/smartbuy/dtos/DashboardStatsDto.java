package com.example.smartbuy.dtos;

public class DashboardStatsDto {
    private Long activeCategoriesCount;
    private Long activeSubCategoriesCount;
    private Long activeProductsCount;

    public DashboardStatsDto() {
    }

    public DashboardStatsDto(Long activeCategoriesCount, Long activeSubCategoriesCount, Long activeProductsCount) {
        this.activeCategoriesCount = activeCategoriesCount;
        this.activeSubCategoriesCount = activeSubCategoriesCount;
        this.activeProductsCount = activeProductsCount;
    }

    public Long getActiveCategoriesCount() {
        return activeCategoriesCount;
    }

    public void setActiveCategoriesCount(Long activeCategoriesCount) {
        this.activeCategoriesCount = activeCategoriesCount;
    }

    public Long getActiveSubCategoriesCount() {
        return activeSubCategoriesCount;
    }

    public void setActiveSubCategoriesCount(Long activeSubCategoriesCount) {
        this.activeSubCategoriesCount = activeSubCategoriesCount;
    }

    public Long getActiveProductsCount() {
        return activeProductsCount;
    }

    public void setActiveProductsCount(Long activeProductsCount) {
        this.activeProductsCount = activeProductsCount;

    }
}
