package com.tms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.dto.request.TravelRequestDto;
import com.tms.dto.response.ApprovalStepResponse;
import com.tms.dto.response.ExpenseResponse;
import com.tms.dto.response.ItineraryItemResponse;
import com.tms.dto.response.TravelRequestResponse;
import com.tms.exception.BadRequestException;
import com.tms.exception.ResourceNotFoundException;
import com.tms.model.AuditLog;
import com.tms.model.TravelRequest;
import com.tms.model.User;
import com.tms.model.enums.TravelStatus;
import com.tms.repository.AuditLogRepository;
import com.tms.repository.TravelRequestRepository;
import com.tms.repository.UserRepository;

@Service
public class TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final PolicyService policyService;

    public TravelRequestService(TravelRequestRepository travelRequestRepository,
                                 UserRepository userRepository,
                                 AuditLogRepository auditLogRepository,
                                 PolicyService policyService) {
        this.travelRequestRepository = travelRequestRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
        this.policyService = policyService;
    }

    @Transactional
    public TravelRequestResponse createDraft(TravelRequestDto dto, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        TravelRequest request = TravelRequest.builder()
                .employee(employee)
                .destination(dto.getDestination())
                .travelFrom(dto.getTravelFrom())
                .travelTo(dto.getTravelTo())
                .purpose(dto.getPurpose())
                .estimatedBudget(dto.getEstimatedBudget())
                .status(TravelStatus.DRAFT)
                .build();

        PolicyService.PolicyResult policy = policyService.check(employee, dto.getEstimatedBudget());

// BLOCK the request if budget exceeds department limit
        if (policy.isViolated()) {
        throw new BadRequestException(
                "Budget exceeded: " + policy.getReason() +
                ". Please reduce your budget and try again."
        );
        }

        request.setPolicyViolated(false);
        request.setPolicyViolationReason(null);

        request = travelRequestRepository.save(request);
        logAudit(request.getId(), employeeId, employee.getName(),
                "CREATED_DRAFT", "Request created as draft");
        return mapToResponse(request);
    }

    @Transactional
    public TravelRequestResponse submitRequest(Long requestId, Long employeeId) {
        TravelRequest request = getRequestAndVerifyOwner(requestId, employeeId);
        if (request.getStatus() != TravelStatus.DRAFT) {
            throw new BadRequestException("Only DRAFT requests can be submitted");
        }
        request.setStatus(TravelStatus.SUBMITTED);
        request = travelRequestRepository.save(request);
        logAudit(requestId, employeeId, request.getEmployee().getName(),
                "SUBMITTED", "Request submitted for approval");
        return mapToResponse(request);
    }

    @Transactional
    public TravelRequestResponse updateDraft(Long requestId, TravelRequestDto dto, Long employeeId) {
        TravelRequest request = getRequestAndVerifyOwner(requestId, employeeId);
        if (request.getStatus() != TravelStatus.DRAFT) {
            throw new BadRequestException("Only DRAFT requests can be edited");
        }
        request.setDestination(dto.getDestination());
        request.setTravelFrom(dto.getTravelFrom());
        request.setTravelTo(dto.getTravelTo());
        request.setPurpose(dto.getPurpose());
        request.setEstimatedBudget(dto.getEstimatedBudget());

        PolicyService.PolicyResult policy = policyService.check(
        request.getEmployee(), dto.getEstimatedBudget());

// BLOCK the update if budget exceeds department limit
        if (policy.isViolated()) {
        throw new BadRequestException(
                "Budget exceeded: " + policy.getReason() +
                ". Please reduce your budget and try again."
        );
        }

        request.setPolicyViolated(false);
        request.setPolicyViolationReason(null);

        request = travelRequestRepository.save(request);
        logAudit(requestId, employeeId, request.getEmployee().getName(),
                "UPDATED_DRAFT", "Draft updated");
        return mapToResponse(request);
    }

    public List<TravelRequestResponse> getMyRequests(Long employeeId) {
        return travelRequestRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TravelRequestResponse getById(Long requestId) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found"));
        return mapToResponse(request);
    }

    // Returns ALL submitted requests (for admin view)
    public List<TravelRequestResponse> getPendingForManager() {
        return travelRequestRepository
                .findByStatusOrderByCreatedAtDesc(TravelStatus.SUBMITTED)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Returns submitted requests filtered by the manager's own department
    public List<TravelRequestResponse> getPendingForManagerByUser(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        return travelRequestRepository
                .findByStatusOrderByCreatedAtDesc(TravelStatus.SUBMITTED)
                .stream()
                .filter(r -> {
                    // Show request if employee reports to this manager
                    // OR employee is in same department as manager
                    User employee = r.getEmployee();
                    boolean sameManager = employee.getManager() != null
                            && employee.getManager().getId().equals(managerId);
                    boolean sameDept = employee.getDepartment() != null
                            && manager.getDepartment() != null
                            && employee.getDepartment().getId()
                               .equals(manager.getDepartment().getId());
                    return sameManager || sameDept;
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TravelRequestResponse> getPendingForFinance() {
        return travelRequestRepository
                .findByStatusOrderByCreatedAtDesc(TravelStatus.MANAGER_APPROVED)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<TravelRequestResponse> getAllRequests() {
        return travelRequestRepository.findAll()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private TravelRequest getRequestAndVerifyOwner(Long requestId, Long employeeId) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found"));
        if (!request.getEmployee().getId().equals(employeeId)) {
            throw new BadRequestException("You do not own this request");
        }
        return request;
    }

    private void logAudit(Long requestId, Long actorId, String actorName,
                           String action, String note) {
        AuditLog log = AuditLog.builder()
                .requestId(requestId).actorId(actorId)
                .actorName(actorName).action(action).note(note)
                .build();
        auditLogRepository.save(log);
    }

    public TravelRequestResponse mapToResponse(TravelRequest r) {
        return TravelRequestResponse.builder()
                .id(r.getId())
                .employeeId(r.getEmployee().getId())
                .employeeName(r.getEmployee().getName())
                .employeeEmail(r.getEmployee().getEmail())
                .departmentName(r.getEmployee().getDepartment() != null
                        ? r.getEmployee().getDepartment().getName() : "N/A")
                .destination(r.getDestination())
                .travelFrom(r.getTravelFrom())
                .travelTo(r.getTravelTo())
                .purpose(r.getPurpose())
                .estimatedBudget(r.getEstimatedBudget())
                .status(r.getStatus().name())
                .policyViolated(r.getPolicyViolated())
                .policyViolationReason(r.getPolicyViolationReason())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .approvalSteps(r.getApprovalSteps().stream().map(a ->
                        ApprovalStepResponse.builder()
                                .id(a.getId())
                                .level(a.getLevel().name())
                                .approverName(a.getApprover() != null
                                        ? a.getApprover().getName() : null)
                                .action(a.getAction() != null
                                        ? a.getAction().name() : null)
                                .comments(a.getComments())
                                .actedAt(a.getActedAt())
                                .createdAt(a.getCreatedAt())
                                .build()).collect(Collectors.toList()))
                .itineraryItems(r.getItineraryItems().stream().map(i ->
                        ItineraryItemResponse.builder()
                                .id(i.getId()).type(i.getType())
                                .details(i.getDetails())
                                .startDt(i.getStartDt()).endDt(i.getEndDt())
                                .location(i.getLocation())
                                .build()).collect(Collectors.toList()))
                .expenses(r.getExpenses().stream().map(e ->
                        ExpenseResponse.builder()
                                .id(e.getId()).category(e.getCategory().name())
                                .amount(e.getAmount()).description(e.getDescription())
                                .proofFileName(e.getProofFileName())
                                .status(e.getStatus()).createdAt(e.getCreatedAt())
                                .build()).collect(Collectors.toList()))
                .build();
    }
}