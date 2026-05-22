package com.tms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tms.model.enums.TravelStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "travel_requests")
public class TravelRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(nullable = false)
    private String destination;

    @Column(name = "travel_from", nullable = false)
    private LocalDate travelFrom;

    @Column(name = "travel_to", nullable = false)
    private LocalDate travelTo;

    @Column(nullable = false)
    private String purpose;

    @Column(name = "estimated_budget", nullable = false)
    private Double estimatedBudget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TravelStatus status = TravelStatus.DRAFT;

    @Column(name = "policy_violated")
    private Boolean policyViolated = false;

    @Column(name = "policy_violation_reason")
    private String policyViolationReason;

    @OneToMany(mappedBy = "travelRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalStep> approvalSteps = new ArrayList<>();

    @OneToMany(mappedBy = "travelRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItineraryItem> itineraryItems = new ArrayList<>();

    @OneToMany(mappedBy = "travelRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public TravelRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getEmployee() { return employee; }
    public void setEmployee(User employee) { this.employee = employee; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDate getTravelFrom() { return travelFrom; }
    public void setTravelFrom(LocalDate travelFrom) { this.travelFrom = travelFrom; }
    public LocalDate getTravelTo() { return travelTo; }
    public void setTravelTo(LocalDate travelTo) { this.travelTo = travelTo; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public Double getEstimatedBudget() { return estimatedBudget; }
    public void setEstimatedBudget(Double v) { this.estimatedBudget = v; }
    public TravelStatus getStatus() { return status; }
    public void setStatus(TravelStatus status) { this.status = status; }
    public Boolean getPolicyViolated() { return policyViolated; }
    public void setPolicyViolated(Boolean v) { this.policyViolated = v; }
    public String getPolicyViolationReason() { return policyViolationReason; }
    public void setPolicyViolationReason(String v) { this.policyViolationReason = v; }
    public List<ApprovalStep> getApprovalSteps() { return approvalSteps; }
    public void setApprovalSteps(List<ApprovalStep> v) { this.approvalSteps = v; }
    public List<ItineraryItem> getItineraryItems() { return itineraryItems; }
    public void setItineraryItems(List<ItineraryItem> v) { this.itineraryItems = v; }
    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) { this.expenses = expenses; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final TravelRequest r = new TravelRequest();
        public Builder employee(User v) { r.employee = v; return this; }
        public Builder destination(String v) { r.destination = v; return this; }
        public Builder travelFrom(LocalDate v) { r.travelFrom = v; return this; }
        public Builder travelTo(LocalDate v) { r.travelTo = v; return this; }
        public Builder purpose(String v) { r.purpose = v; return this; }
        public Builder estimatedBudget(Double v) { r.estimatedBudget = v; return this; }
        public Builder status(TravelStatus v) { r.status = v; return this; }
        public Builder policyViolated(Boolean v) { r.policyViolated = v; return this; }
        public Builder policyViolationReason(String v) { r.policyViolationReason = v; return this; }
        public TravelRequest build() { return r; }
    }
}