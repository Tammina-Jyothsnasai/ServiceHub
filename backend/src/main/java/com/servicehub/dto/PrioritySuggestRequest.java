package com.servicehub.dto;

public class PrioritySuggestRequest {
    private String description;

    public PrioritySuggestRequest() {}
    public PrioritySuggestRequest(String description) { this.description = description; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
