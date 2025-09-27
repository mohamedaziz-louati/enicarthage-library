package com.enicarthage.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventCreateRequest {
    
    private String title;
    
    private String description;
    
    private String location;
    
    private String type;
    
    @JsonProperty("startDate")
    private String startDateStr;
    
    @JsonProperty("endDate")
    private String endDateStr;
    
    private Boolean registrationRequired;
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getStartDateStr() {
        return startDateStr;
    }
    
    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }
    
    public String getEndDateStr() {
        return endDateStr;
    }
    
    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }
    
    public Boolean getRegistrationRequired() {
        return registrationRequired;
    }
    
    public void setRegistrationRequired(Boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }
}
