package com.servicehub.dto;

import jakarta.validation.constraints.NotNull;

public class AssignStaffDTO {

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    public AssignStaffDTO() {}

    public AssignStaffDTO(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
}
