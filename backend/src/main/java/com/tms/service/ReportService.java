package com.tms.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tms.dto.response.TravelRequestResponse;
import com.tms.model.Department;
import com.tms.model.enums.TravelStatus;
import com.tms.repository.AuditLogRepository;
import com.tms.repository.DepartmentRepository;
import com.tms.repository.TravelRequestRepository;

@Service
public class ReportService {

    private final TravelRequestRepository travelRequestRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditLogRepository auditLogRepository;
    private final TravelRequestService travelRequestService;

    public ReportService(TravelRequestRepository travelRequestRepository,
                         DepartmentRepository departmentRepository,
                         AuditLogRepository auditLogRepository,
                         TravelRequestService travelRequestService) {
        this.travelRequestRepository = travelRequestRepository;
        this.departmentRepository = departmentRepository;
        this.auditLogRepository = auditLogRepository;
        this.travelRequestService = travelRequestService;
    }

    public List<TravelRequestResponse> getAllApprovedRequests() {
        return travelRequestRepository
                .findByStatusInOrderByCreatedAtDesc(List.of(TravelStatus.FINANCE_APPROVED, TravelStatus.CLOSED))
                .stream().map(travelRequestService::mapToResponse).collect(Collectors.toList());
    }

    public List<TravelRequestResponse> getEmployeeHistory(Long employeeId) {
        return travelRequestRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream().map(travelRequestService::mapToResponse).collect(Collectors.toList());
    }

    public List<DepartmentSpendReport> getDepartmentSpendReport() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(dept -> {
            Double budget = travelRequestRepository.sumBudgetByDepartment(dept.getId());
            Double actual = travelRequestRepository.sumExpensesByDepartment(dept.getId());
            return new DepartmentSpendReport(
                    dept.getId(), dept.getName(),
                    budget != null ? budget : 0.0,
                    actual != null ? actual : 0.0,
                    dept.getBudgetLimit()
            );
        }).collect(Collectors.toList());
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalRequests", travelRequestRepository.count());
        stats.put("pendingManagerApproval",
                travelRequestRepository.findByStatusOrderByCreatedAtDesc(TravelStatus.SUBMITTED).size());
        stats.put("pendingFinanceApproval",
                travelRequestRepository.findByStatusOrderByCreatedAtDesc(TravelStatus.MANAGER_APPROVED).size());
        stats.put("approved",
                travelRequestRepository.findByStatusOrderByCreatedAtDesc(TravelStatus.FINANCE_APPROVED).size());
        stats.put("closed",
                travelRequestRepository.findByStatusOrderByCreatedAtDesc(TravelStatus.CLOSED).size());
        return stats;
    }

    public static class DepartmentSpendReport {
        private Long departmentId;
        private String departmentName;
        private Double approvedBudget;
        private Double actualExpenses;
        private Double budgetLimit;

        public DepartmentSpendReport(Long departmentId, String departmentName,
                                     Double approvedBudget, Double actualExpenses, Double budgetLimit) {
            this.departmentId = departmentId;
            this.departmentName = departmentName;
            this.approvedBudget = approvedBudget;
            this.actualExpenses = actualExpenses;
            this.budgetLimit = budgetLimit;
        }

        public Long getDepartmentId() { return departmentId; }
        public String getDepartmentName() { return departmentName; }
        public Double getApprovedBudget() { return approvedBudget; }
        public Double getActualExpenses() { return actualExpenses; }
        public Double getBudgetLimit() { return budgetLimit; }
    }
}