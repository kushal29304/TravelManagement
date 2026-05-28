package com.tms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tms.dto.request.ExpenseRequest;
import com.tms.dto.response.ExpenseResponse;
import com.tms.exception.BadRequestException;
import com.tms.exception.ResourceNotFoundException;
import com.tms.model.AuditLog;
import com.tms.model.Expense;
import com.tms.model.TravelRequest;
import com.tms.model.enums.ExpenseCategory;
import com.tms.model.enums.TravelStatus;
import com.tms.repository.AuditLogRepository;
import com.tms.repository.ExpenseRepository;
import com.tms.repository.TravelRequestRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final AuditLogRepository auditLogRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ExpenseService(ExpenseRepository expenseRepository,
                          TravelRequestRepository travelRequestRepository,
                          AuditLogRepository auditLogRepository) {
        this.expenseRepository = expenseRepository;
        this.travelRequestRepository = travelRequestRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public ExpenseResponse addExpense(Long requestId, ExpenseRequest dto,
                                      MultipartFile file, Long userId, String userName) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found"));

        if (request.getStatus() != TravelStatus.FINANCE_APPROVED &&
            request.getStatus() != TravelStatus.CLOSED) {
            throw new BadRequestException("Expenses can only be added to approved requests");
        }

        String proofPath = null;
        String proofFileName = null;

        if (file != null && !file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), uploadPath.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                proofPath = uploadDir + "/" + filename;
                proofFileName = file.getOriginalFilename();
            } catch (IOException e) {
                throw new BadRequestException("Failed to upload file: " + e.getMessage());
            }
        }

        Expense expense = Expense.builder()
                .travelRequest(request)
                .category(ExpenseCategory.valueOf(dto.getCategory().toUpperCase()))
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .proofPath(proofPath)
                .proofFileName(proofFileName)
                .status("PENDING")
                .build();

        expense = expenseRepository.save(expense);

        AuditLog log = AuditLog.builder()
                .requestId(requestId).actorId(userId).actorName(userName)
                .action("EXPENSE_ADDED")
                .note(String.format("Expense: %s - %.2f", dto.getCategory(), dto.getAmount()))
                .build();
        auditLogRepository.save(log);

        return toResponse(expense);
    }

    @Transactional
    public ExpenseResponse markReimbursed(Long expenseId, Long userId, String userName) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        expense.setStatus("REIMBURSED");
        expense = expenseRepository.save(expense);

        AuditLog log = AuditLog.builder()
                .requestId(expense.getTravelRequest().getId())
                .actorId(userId).actorName(userName)
                .action("EXPENSE_REIMBURSED").note("Expense marked as reimbursed")
                .build();
        auditLogRepository.save(log);
        return toResponse(expense);
    }

    public List<ExpenseResponse> getByRequest(Long requestId) {
        return expenseRepository.findByTravelRequestId(requestId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ExpenseResponse toResponse(Expense e) {
        return ExpenseResponse.builder()
                .id(e.getId()).category(e.getCategory().name())
                .amount(e.getAmount()).description(e.getDescription())
                .proofFileName(e.getProofFileName()).status(e.getStatus())
                .createdAt(e.getCreatedAt()).build();
    }
}