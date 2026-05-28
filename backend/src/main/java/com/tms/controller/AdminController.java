package com.tms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.dto.request.RegisterRequest;
import com.tms.dto.response.ApiResponse;
import com.tms.model.AuditLog;
import com.tms.model.Department;
import com.tms.model.User;
import com.tms.repository.AuditLogRepository;
import com.tms.repository.DepartmentRepository;
import com.tms.repository.UserRepository;
import com.tms.service.AuthService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditLogRepository auditLogRepository;
    private final AuthService authService;

// Update constructor to include AuthService
public AdminController(UserRepository userRepository,
                       DepartmentRepository departmentRepository,
                       AuditLogRepository auditLogRepository,
                       AuthService authService) {
    this.userRepository = userRepository;
    this.departmentRepository = departmentRepository;
    this.auditLogRepository = auditLogRepository;
    this.authService = authService;
}

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userRepository.findAll()));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Department>>> getDepartments() {
        return ResponseEntity.ok(ApiResponse.success(departmentRepository.findAll()));
    }

    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<Department>> createDepartment(@RequestBody Department dept) {
        return ResponseEntity.ok(ApiResponse.success(departmentRepository.save(dept), "Department created"));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<Department>> updateDepartment(
            @PathVariable Long id, @RequestBody Department dept) {
        dept.setId(id);
        return ResponseEntity.ok(ApiResponse.success(departmentRepository.save(dept), "Department updated"));
    }

    @GetMapping("/audit-logs/{requestId}")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs(@PathVariable Long requestId) {
        return ResponseEntity.ok(ApiResponse.success(
                auditLogRepository.findByRequestIdOrderByCreatedAtDesc(requestId)));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAllAuditLogs() {
        return ResponseEntity.ok(ApiResponse.success(auditLogRepository.findAll()));
    }

    @PostMapping("/users")
public ResponseEntity<ApiResponse<String>> createUser(
        @RequestBody RegisterRequest request) {
    try {
        authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User created successfully"));
    } catch (Exception e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
    }
}

@DeleteMapping("/users/{id}")
public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
    if (!userRepository.existsById(id)) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("User not found"));
    }
    userRepository.deleteById(id);
    return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
}
}