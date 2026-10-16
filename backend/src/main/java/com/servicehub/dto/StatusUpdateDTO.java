package com.servicehub.dto;

import com.servicehub.enums.Priority;
import com.servicehub.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateDTO {

    @NotNull(message = "Status is required")
    private RequestStatus status;

    private Priority priority;
    private String resolutionNotes;

    public StatusUpdateDTO() {}

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
}
