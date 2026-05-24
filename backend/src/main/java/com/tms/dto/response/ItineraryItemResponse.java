package com.tms.dto.response;

import java.time.LocalDateTime;

public class ItineraryItemResponse {
    private Long id;
    private String type;
    private String details;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private String location;

    public ItineraryItemResponse() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getStartDt() { return startDt; }
    public void setStartDt(LocalDateTime startDt) { this.startDt = startDt; }
    public LocalDateTime getEndDt() { return endDt; }
    public void setEndDt(LocalDateTime endDt) { this.endDt = endDt; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ItineraryItemResponse r = new ItineraryItemResponse();
        public Builder id(Long v) { r.id = v; return this; }
        public Builder type(String v) { r.type = v; return this; }
        public Builder details(String v) { r.details = v; return this; }
        public Builder startDt(LocalDateTime v) { r.startDt = v; return this; }
        public Builder endDt(LocalDateTime v) { r.endDt = v; return this; }
        public Builder location(String v) { r.location = v; return this; }
        public ItineraryItemResponse build() { return r; }
    }
}