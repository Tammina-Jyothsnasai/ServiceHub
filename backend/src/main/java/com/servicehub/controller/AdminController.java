package com.servicehub.controller;

import com.servicehub.dto.*;
import com.servicehub.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(originPatterns = "*")
public class AdminController {

    private final RequestService requestService;

    @Autowired
    public AdminController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public ResponseEntity<List<ServiceRequestDTO>> getAllRequests() {
        List<ServiceRequestDTO> list = requestService.getAllRequests();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/requests/{id}/assign")
    public ResponseEntity<ServiceRequestDTO> assignStaff(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody AssignStaffDTO dto) {
        ServiceRequestDTO updated = requestService.assignStaff(id, dto.getStaffId());
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/requests/{id}/status")
    public ResponseEntity<ServiceRequestDTO> updateStatus(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody StatusUpdateDTO dto) {
        ServiceRequestDTO updated = requestService.updateStatus(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> getStatistics() {
        StatisticsDTO stats = requestService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
