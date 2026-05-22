package com.tms.model;

import java.time.LocalDateTime;

import com.tms.model.enums.ExpenseCategory;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private Double amount;

    private String description;

    @Column(name = "proof_path")
    private String proofPath;

    @Column(name = "proof_file_name")
    private String proofFileName;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Expense() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest v) { this.travelRequest = v; }
    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProofPath() { return proofPath; }
    public void setProofPath(String proofPath) { this.proofPath = proofPath; }
    public String getProofFileName() { return proofFileName; }
    public void setProofFileName(String v) { this.proofFileName = v; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Expense e = new Expense();
        public Builder travelRequest(TravelRequest v) { e.travelRequest = v; return this; }
        public Builder category(ExpenseCategory v) { e.category = v; return this; }
        public Builder amount(Double v) { e.amount = v; return this; }
        public Builder description(String v) { e.description = v; return this; }
        public Builder proofPath(String v) { e.proofPath = v; return this; }
        public Builder proofFileName(String v) { e.proofFileName = v; return this; }
        public Builder status(String v) { e.status = v; return this; }
        public Expense build() { return e; }
    }
}