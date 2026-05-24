package com.tms.dto.response;

import java.time.LocalDateTime;

public class ExpenseResponse {
    private Long id;
    private String category;
    private Double amount;
    private String description;
    private String proofFileName;
    private String status;
    private LocalDateTime createdAt;

    public ExpenseResponse() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProofFileName() { return proofFileName; }
    public void setProofFileName(String proofFileName) { this.proofFileName = proofFileName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ExpenseResponse r = new ExpenseResponse();
        public Builder id(Long v) { r.id = v; return this; }
        public Builder category(String v) { r.category = v; return this; }
        public Builder amount(Double v) { r.amount = v; return this; }
        public Builder description(String v) { r.description = v; return this; }
        public Builder proofFileName(String v) { r.proofFileName = v; return this; }
        public Builder status(String v) { r.status = v; return this; }
        public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
        public ExpenseResponse build() { return r; }
    }
}