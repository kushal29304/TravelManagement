package com.tms.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.dto.request.ApprovalRequest;
import com.tms.dto.response.TravelRequestResponse;
import com.tms.exception.BadRequestException;
import com.tms.exception.ResourceNotFoundException;
import com.tms.model.ApprovalStep;
import com.tms.model.AuditLog;
import com.tms.model.TravelRequest;
import com.tms.model.User;
import com.tms.model.enums.ApprovalLevel;
import com.tms.model.enums.TravelStatus;
import com.tms.repository.ApprovalStepRepository;
import com.tms.repository.AuditLogRepository;
import com.tms.repository.TravelRequestRepository;
import com.tms.repository.UserRepository;

@Service
public class ApprovalService {

    private final TravelRequestRepository travelRequestRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final TravelRequestService travelRequestService;

    public ApprovalService(TravelRequestRepository travelRequestRepository,
                           ApprovalStepRepository approvalStepRepository,
                           UserRepository userRepository,
                           AuditLogRepository auditLogRepository,
                           TravelRequestService travelRequestService) {
        this.travelRequestRepository = travelRequestRepository;
        this.approvalStepRepository = approvalStepRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
        this.travelRequestService = travelRequestService;
    }

    @Transactional
    public TravelRequestResponse processManagerApproval(Long requestId, ApprovalRequest dto, Long approverId) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found"));

        if (request.getStatus() != TravelStatus.SUBMITTED) {
            throw new BadRequestException("Request is not in SUBMITTED state");
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("Approver not found"));

        boolean isApprove = "APPROVE".equalsIgnoreCase(dto.getAction());
        TravelStatus newStatus = isApprove ? TravelStatus.MANAGER_APPROVED : TravelStatus.MANAGER_REJECTED;

        ApprovalStep step = ApprovalStep.builder()
                .travelRequest(request)
                .approver(approver)
                .level(ApprovalLevel.MANAGER)
                .action(newStatus)
                .comments(dto.getComments())
                .actedAt(LocalDateTime.now())
                .build();
        approvalStepRepository.save(step);
        request.getApprovalSteps().add(step);
        request.setStatus(newStatus);
        travelRequestRepository.save(request);

        logAudit(requestId, approverId, approver.getName(),
                isApprove ? "MANAGER_APPROVED" : "MANAGER_REJECTED", dto.getComments());
        return travelRequestService.mapToResponse(request);
    }

    @Transactional
    public TravelRequestResponse processFinanceApproval(Long requestId, ApprovalRequest dto, Long approverId) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found"));

        if (request.getStatus() != TravelStatus.MANAGER_APPROVED) {
            throw new BadRequestException("Request is not in MANAGER_APPROVED state");
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("Approver not found"));

        boolean isApprove = "APPROVE".equalsIgnoreCase(dto.getAction());
        TravelStatus newStatus = isApprove ? TravelStatus.FINANCE_APPROVED : TravelStatus.FINANCE_REJECTED;

        ApprovalStep step = ApprovalStep.builder()
                .travelRequest(request)
                .approver(approver)
                .level(ApprovalLevel.FINANCE)
                .action(newStatus)
                .comments(dto.getComments())
                .actedAt(LocalDateTime.now())
                .build();
        approvalStepRepository.save(step);
        request.getApprovalSteps().add(step);
        request.setStatus(newStatus);
        travelRequestRepository.save(request);

        logAudit(requestId, approverId, approver.getName(),
                isApprove ? "FINANCE_APPROVED" : "FINANCE_REJECTED", dto.getComments());
        return travelRequestService.mapToResponse(request);
    }

    @Transactional
    public TravelRequestResponse closeRequest(Long requestId, Long userId) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found"));

        if (request.getStatus() != TravelStatus.FINANCE_APPROVED) {
            throw new BadRequestException("Only FINANCE_APPROVED requests can be closed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        request.setStatus(TravelStatus.CLOSED);
        travelRequestRepository.save(request);
        logAudit(requestId, userId, user.getName(), "CLOSED", "Request closed after expense settlement");
        return travelRequestService.mapToResponse(request);
    }

    private void logAudit(Long requestId, Long actorId, String actorName, String action, String note) {
        AuditLog log = AuditLog.builder()
                .requestId(requestId)
                .actorId(actorId)
                .actorName(actorName)
                .action(action)
                .note(note)
                .build();
        auditLogRepository.save(log);
    }
}