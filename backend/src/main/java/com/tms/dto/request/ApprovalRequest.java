package com.tms.dto.request;

public class ApprovalRequest {
    private String action;
    private String comments;
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}