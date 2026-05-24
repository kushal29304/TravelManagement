package com.tms.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ItineraryItemRequest {
    @NotBlank private String type;
    @NotBlank private String details;
    private String startDt;
    private String endDt;
    private String location;
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getStartDt() { return startDt; }
    public void setStartDt(String startDt) { this.startDt = startDt; }
    public String getEndDt() { return endDt; }
    public void setEndDt(String endDt) { this.endDt = endDt; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}