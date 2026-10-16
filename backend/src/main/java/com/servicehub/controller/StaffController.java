package com.servicehub.controller;

import com.servicehub.dto.StaffDTO;
import com.servicehub.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(originPatterns = "*")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/active")
    public ResponseEntity<List<StaffDTO>> getActiveStaff() {
        return ResponseEntity.ok(staffService.getActiveStaff());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @PostMapping
    public ResponseEntity<StaffDTO> addStaff(@Valid @RequestBody StaffDTO dto) {
        StaffDTO created = staffService.addStaff(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDTO> updateStaff(@PathVariable(name = "id") Long id, @Valid @RequestBody StaffDTO dto) {
        StaffDTO updated = staffService.updateStaff(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateStaff(@PathVariable(name = "id") Long id) {
        staffService.deactivateStaff(id);
        return ResponseEntity.noContent().build();
    }
}
