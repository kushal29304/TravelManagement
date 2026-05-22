package com.tms.model;

import java.time.LocalDateTime;

import com.tms.model.enums.ApprovalLevel;
import com.tms.model.enums.TravelStatus;

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
@Table(name = "approval_steps")
public class ApprovalStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalLevel level;

    @Enumerated(EnumType.STRING)
    private TravelStatus action;

    private String comments;

    @Column(name = "acted_at")
    private LocalDateTime actedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public ApprovalStep() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest v) { this.travelRequest = v; }
    public User getApprover() { return approver; }
    public void setApprover(User approver) { this.approver = approver; }
    public ApprovalLevel getLevel() { return level; }
    public void setLevel(ApprovalLevel level) { this.level = level; }
    public TravelStatus getAction() { return action; }
    public void setAction(TravelStatus action) { this.action = action; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getActedAt() { return actedAt; }
    public void setActedAt(LocalDateTime actedAt) { this.actedAt = actedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ApprovalStep a = new ApprovalStep();
        public Builder travelRequest(TravelRequest v) { a.travelRequest = v; return this; }
        public Builder approver(User v) { a.approver = v; return this; }
        public Builder level(ApprovalLevel v) { a.level = v; return this; }
        public Builder action(TravelStatus v) { a.action = v; return this; }
        public Builder comments(String v) { a.comments = v; return this; }
        public Builder actedAt(LocalDateTime v) { a.actedAt = v; return this; }
        public ApprovalStep build() { return a; }
    }
}