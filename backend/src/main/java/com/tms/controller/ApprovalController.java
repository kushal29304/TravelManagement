package com.tms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.dto.request.ApprovalRequest;
import com.tms.dto.response.ApiResponse;
import com.tms.dto.response.TravelRequestResponse;
import com.tms.service.ApprovalService;
import com.tms.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;
    private final JwtUtil jwtUtil;

    public ApprovalController(ApprovalService approvalService, JwtUtil jwtUtil) {
        this.approvalService = approvalService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/manager/{requestId}")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> managerApproval(
            @PathVariable Long requestId,
            @RequestBody ApprovalRequest dto,
            HttpServletRequest req) {
        Long userId = extractUserId(req);
        return ResponseEntity.ok(ApiResponse.success(
                approvalService.processManagerApproval(requestId, dto, userId)));
    }

    @PostMapping("/finance/{requestId}")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> financeApproval(
            @PathVariable Long requestId,
            @RequestBody ApprovalRequest dto,
            HttpServletRequest req) {
        Long userId = extractUserId(req);
        return ResponseEntity.ok(ApiResponse.success(
                approvalService.processFinanceApproval(requestId, dto, userId)));
    }

    @PostMapping("/close/{requestId}")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> closeRequest(
            @PathVariable Long requestId, HttpServletRequest req) {
        Long userId = extractUserId(req);
        return ResponseEntity.ok(ApiResponse.success(
                approvalService.closeRequest(requestId, userId)));
    }

    private Long extractUserId(HttpServletRequest req) {
        String token = req.getHeader("Authorization").substring(7);
        return jwtUtil.extractUserId(token);
    }
}