package com.servicehub.dto;

import com.servicehub.enums.Priority;

public class PrioritySuggestResponse {

    private Priority suggestedPriority;
    private String reason;

    public PrioritySuggestResponse() {}

    public PrioritySuggestResponse(Priority suggestedPriority, String reason) {
        this.suggestedPriority = suggestedPriority;
        this.reason = reason;
    }

    public Priority getSuggestedPriority() { return suggestedPriority; }
    public void setSuggestedPriority(Priority suggestedPriority) { this.suggestedPriority = suggestedPriority; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
