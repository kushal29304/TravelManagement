package com.tms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.dto.request.TravelRequestDto;
import com.tms.dto.response.ApiResponse;
import com.tms.dto.response.TravelRequestResponse;
import com.tms.service.TravelRequestService;
import com.tms.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/travel")
public class TravelRequestController {

    private final TravelRequestService travelRequestService;
    private final JwtUtil jwtUtil;

    public TravelRequestController(TravelRequestService travelRequestService,
                                    JwtUtil jwtUtil) {
        this.travelRequestService = travelRequestService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/draft")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> createDraft(
            @Valid @RequestBody TravelRequestDto dto, HttpServletRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                travelRequestService.createDraft(dto, extractUserId(req))));
    }

    @PutMapping("/{id}/draft")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> updateDraft(
            @PathVariable Long id,
            @Valid @RequestBody TravelRequestDto dto,
            HttpServletRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                travelRequestService.updateDraft(id, dto, extractUserId(req))));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> submit(
            @PathVariable Long id, HttpServletRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                travelRequestService.submitRequest(id, extractUserId(req)),
                "Request submitted"));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> getMyRequests(
            HttpServletRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                travelRequestService.getMyRequests(extractUserId(req))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(travelRequestService.getById(id)));
    }

    // Used by manager - shows only requests from their department/team
    @GetMapping("/pending/manager/my")
    public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> getPendingForMyTeam(
            HttpServletRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                travelRequestService.getPendingForManagerByUser(extractUserId(req))));
    }

    // Used by admin - shows ALL submitted requests
    @GetMapping("/pending/manager")
    public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> getPendingForManager() {
        return ResponseEntity.ok(ApiResponse.success(
                travelRequestService.getPendingForManager()));
    }

    @GetMapping("/pending/finance")
    public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> getPendingForFinance() {
        return ResponseEntity.ok(ApiResponse.success(
                travelRequestService.getPendingForFinance()));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(travelRequestService.getAllRequests()));
    }

    private Long extractUserId(HttpServletRequest req) {
        String token = req.getHeader("Authorization").substring(7);
        return jwtUtil.extractUserId(token);
    }
}