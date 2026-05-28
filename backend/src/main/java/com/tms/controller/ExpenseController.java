package com.tms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tms.dto.request.ExpenseRequest;
import com.tms.dto.response.ApiResponse;
import com.tms.dto.response.ExpenseResponse;
import com.tms.service.ExpenseService;
import com.tms.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final JwtUtil jwtUtil;

    public ExpenseController(ExpenseService expenseService, JwtUtil jwtUtil) {
        this.expenseService = expenseService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{requestId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> addExpense(
            @PathVariable Long requestId,
            @RequestPart("expense") ExpenseRequest dto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest req) {
        String token = req.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        String userName = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(ApiResponse.success(
                expenseService.addExpense(requestId, dto, file, userId, userName)));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getExpenses(
            @PathVariable Long requestId) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.getByRequest(requestId)));
    }

    @PatchMapping("/{expenseId}/reimburse")
    public ResponseEntity<ApiResponse<ExpenseResponse>> markReimbursed(
            @PathVariable Long expenseId, HttpServletRequest req) {
        String token = req.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        String userName = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(ApiResponse.success(
                expenseService.markReimbursed(expenseId, userId, userName)));
    }
}