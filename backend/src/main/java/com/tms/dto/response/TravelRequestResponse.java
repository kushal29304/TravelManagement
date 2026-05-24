package com.tms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TravelRequestResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String departmentName;
    private String destination;
    private LocalDate travelFrom;
    private LocalDate travelTo;
    private String purpose;
    private Double estimatedBudget;
    private String status;
    private Boolean policyViolated;
    private String policyViolationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ApprovalStepResponse> approvalSteps;
    private List<ItineraryItemResponse> itineraryItems;
    private List<ExpenseResponse> expenses;

    public TravelRequestResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmployeeEmail() { return employeeEmail; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDate getTravelFrom() { return travelFrom; }
    public void setTravelFrom(LocalDate travelFrom) { this.travelFrom = travelFrom; }
    public LocalDate getTravelTo() { return travelTo; }
    public void setTravelTo(LocalDate travelTo) { this.travelTo = travelTo; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public Double getEstimatedBudget() { return estimatedBudget; }
    public void setEstimatedBudget(Double estimatedBudget) { this.estimatedBudget = estimatedBudget; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getPolicyViolated() { return policyViolated; }
    public void setPolicyViolated(Boolean policyViolated) { this.policyViolated = policyViolated; }
    public String getPolicyViolationReason() { return policyViolationReason; }
    public void setPolicyViolationReason(String r) { this.policyViolationReason = r; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<ApprovalStepResponse> getApprovalSteps() { return approvalSteps; }
    public void setApprovalSteps(List<ApprovalStepResponse> approvalSteps) { this.approvalSteps = approvalSteps; }
    public List<ItineraryItemResponse> getItineraryItems() { return itineraryItems; }
    public void setItineraryItems(List<ItineraryItemResponse> itineraryItems) { this.itineraryItems = itineraryItems; }
    public List<ExpenseResponse> getExpenses() { return expenses; }
    public void setExpenses(List<ExpenseResponse> expenses) { this.expenses = expenses; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final TravelRequestResponse r = new TravelRequestResponse();
        public Builder id(Long v) { r.id = v; return this; }
        public Builder employeeId(Long v) { r.employeeId = v; return this; }
        public Builder employeeName(String v) { r.employeeName = v; return this; }
        public Builder employeeEmail(String v) { r.employeeEmail = v; return this; }
        public Builder departmentName(String v) { r.departmentName = v; return this; }
        public Builder destination(String v) { r.destination = v; return this; }
        public Builder travelFrom(LocalDate v) { r.travelFrom = v; return this; }
        public Builder travelTo(LocalDate v) { r.travelTo = v; return this; }
        public Builder purpose(String v) { r.purpose = v; return this; }
        public Builder estimatedBudget(Double v) { r.estimatedBudget = v; return this; }
        public Builder status(String v) { r.status = v; return this; }
        public Builder policyViolated(Boolean v) { r.policyViolated = v; return this; }
        public Builder policyViolationReason(String v) { r.policyViolationReason = v; return this; }
        public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
        public Builder updatedAt(LocalDateTime v) { r.updatedAt = v; return this; }
        public Builder approvalSteps(List<ApprovalStepResponse> v) { r.approvalSteps = v; return this; }
        public Builder itineraryItems(List<ItineraryItemResponse> v) { r.itineraryItems = v; return this; }
        public Builder expenses(List<ExpenseResponse> v) { r.expenses = v; return this; }
        public TravelRequestResponse build() { return r; }
    }
}