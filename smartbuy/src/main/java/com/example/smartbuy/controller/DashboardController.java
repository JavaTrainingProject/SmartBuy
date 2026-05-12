package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.DashboardStatsDto;
import com.example.smartbuy.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public DashboardStatsDto getDashboardStats(){
        return dashboardService.getDashboardStats();
    }

}
