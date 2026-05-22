package com.tms.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "itinerary_items")
public class ItineraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String details;

    @Column(name = "start_dt")
    private LocalDateTime startDt;

    @Column(name = "end_dt")
    private LocalDateTime endDt;

    private String location;

    public ItineraryItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest v) { this.travelRequest = v; }
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
        private final ItineraryItem i = new ItineraryItem();
        public Builder travelRequest(TravelRequest v) { i.travelRequest = v; return this; }
        public Builder type(String v) { i.type = v; return this; }
        public Builder details(String v) { i.details = v; return this; }
        public Builder startDt(LocalDateTime v) { i.startDt = v; return this; }
        public Builder endDt(LocalDateTime v) { i.endDt = v; return this; }
        public Builder location(String v) { i.location = v; return this; }
        public ItineraryItem build() { return i; }
    }
}