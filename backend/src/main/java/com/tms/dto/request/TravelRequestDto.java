package com.tms.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TravelRequestDto {
    @NotBlank private String destination;
    @NotNull private LocalDate travelFrom;
    @NotNull private LocalDate travelTo;
    @NotBlank private String purpose;
    @NotNull @Positive private Double estimatedBudget;

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
}