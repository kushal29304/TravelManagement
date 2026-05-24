package com.tms.dto.response;

import java.time.LocalDateTime;

public class ApprovalStepResponse {
    private Long id;
    private String level;
    private String approverName;
    private String action;
    private String comments;
    private LocalDateTime actedAt;
    private LocalDateTime createdAt;

    public ApprovalStepResponse() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getActedAt() { return actedAt; }
    public void setActedAt(LocalDateTime actedAt) { this.actedAt = actedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ApprovalStepResponse r = new ApprovalStepResponse();
        public Builder id(Long v) { r.id = v; return this; }
        public Builder level(String v) { r.level = v; return this; }
        public Builder approverName(String v) { r.approverName = v; return this; }
        public Builder action(String v) { r.action = v; return this; }
        public Builder comments(String v) { r.comments = v; return this; }
        public Builder actedAt(LocalDateTime v) { r.actedAt = v; return this; }
        public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
        public ApprovalStepResponse build() { return r; }
    }
}