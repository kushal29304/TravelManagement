package com.tms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.dto.response.ApiResponse;
import com.tms.dto.response.TravelRequestResponse;
import com.tms.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/approved")
    public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> approvedRequests() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getAllApprovedRequests()));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> employeeHistory(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getEmployeeHistory(employeeId)));
    }

    @GetMapping("/department-spend")
    public ResponseEntity<ApiResponse<List<ReportService.DepartmentSpendReport>>> departmentSpend() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getDepartmentSpendReport()));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> dashboard() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getDashboardStats()));
    }
}