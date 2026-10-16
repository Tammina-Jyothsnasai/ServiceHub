package com.servicehub.service;

import com.servicehub.dto.StaffDTO;
import com.servicehub.entity.Staff;
import com.servicehub.enums.StaffStatus;
import com.servicehub.exception.BadRequestException;
import com.servicehub.exception.ResourceNotFoundException;
import com.servicehub.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StaffDTO> getActiveStaff() {
        return staffRepository.findByStatus(StaffStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StaffDTO getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));
        return convertToDTO(staff);
    }

    public StaffDTO addStaff(StaffDTO staffDTO) {
        if (staffRepository.existsByEmail(staffDTO.getEmail().trim().toLowerCase())) {
            throw new BadRequestException("Staff with this email already exists");
        }

        Staff staff = new Staff();
        staff.setName(staffDTO.getName().trim());
        staff.setEmail(staffDTO.getEmail().trim().toLowerCase());
        staff.setDepartment(staffDTO.getDepartment());
        staff.setPhone(staffDTO.getPhone().trim());
        staff.setStatus(staffDTO.getStatus() != null ? staffDTO.getStatus() : StaffStatus.ACTIVE);

        Staff saved = staffRepository.save(staff);
        return convertToDTO(saved);
    }

    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setName(staffDTO.getName().trim());
        staff.setDepartment(staffDTO.getDepartment());
        staff.setPhone(staffDTO.getPhone().trim());
        if (staffDTO.getStatus() != null) {
            staff.setStatus(staffDTO.getStatus());
        }

        Staff updated = staffRepository.save(staff);
        return convertToDTO(updated);
    }

    public void deactivateStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));
        staff.setStatus(StaffStatus.INACTIVE);
        staffRepository.save(staff);
    }

    public StaffDTO convertToDTO(Staff staff) {
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setName(staff.getName());
        dto.setEmail(staff.getEmail());
        dto.setDepartment(staff.getDepartment());
        dto.setPhone(staff.getPhone());
        dto.setStatus(staff.getStatus());
        return dto;
    }
}
