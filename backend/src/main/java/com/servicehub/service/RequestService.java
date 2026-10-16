package com.servicehub.service;

import com.servicehub.dto.*;
import com.servicehub.entity.ServiceRequest;
import com.servicehub.entity.Staff;
import com.servicehub.entity.User;
import com.servicehub.enums.Category;
import com.servicehub.enums.Priority;
import com.servicehub.enums.RequestStatus;
import com.servicehub.enums.Role;
import com.servicehub.exception.BadRequestException;
import com.servicehub.exception.ResourceNotFoundException;
import com.servicehub.repository.FeedbackRepository;
import com.servicehub.repository.ServiceRequestRepository;
import com.servicehub.repository.StaffRepository;
import com.servicehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final ServiceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificationService notificationService;

    @Autowired
    public RequestService(ServiceRequestRepository requestRepository,
                          UserRepository userRepository,
                          StaffRepository staffRepository,
                          FeedbackRepository feedbackRepository,
                          NotificationService notificationService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificationService = notificationService;
    }

    public ServiceRequestDTO createRequest(CreateRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ServiceRequest request = new ServiceRequest();
        request.setUser(user);
        request.setTitle(dto.getTitle().trim());
        request.setDescription(dto.getDescription().trim());
        request.setCategory(dto.getCategory());
        request.setPriority(dto.getPriority());
        request.setLocation(dto.getLocation().trim());
        request.setStatus(RequestStatus.PENDING);

        String ticketId = "SR" + (int)(1000 + (Math.random() * 9000));
        request.setTicketId(ticketId);

        ServiceRequest saved = requestRepository.save(request);

        notificationService.createNotification(
                user, saved, "Your request " + ticketId + " (" + saved.getTitle() + ") has been created successfully."
        );

        return convertToDTO(saved);
    }

    public List<ServiceRequestDTO> getUserRequests(Long userId) {
        return requestRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceRequestDTO> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ServiceRequestDTO getRequestById(Long id) {
        ServiceRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found with ID: " + id));
        return convertToDTO(request);
    }

    public ServiceRequestDTO getRequestByTicketId(String ticketId) {
        ServiceRequest request = requestRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found with ticket ID: " + ticketId));
        return convertToDTO(request);
    }

    public ServiceRequestDTO assignStaff(Long requestId, Long staffId) {
        ServiceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));

        request.setAssignedStaff(staff);
        if (request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.ASSIGNED);
        }

        ServiceRequest updated = requestRepository.save(request);

        notificationService.createNotification(
                request.getUser(), updated,
                "Request " + request.getTicketId() + " has been assigned to " + staff.getName() + " (" + staff.getDepartment() + ")."
        );

        return convertToDTO(updated);
    }

    public ServiceRequestDTO updateStatus(Long requestId, StatusUpdateDTO dto) {
        ServiceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));

        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
            if (dto.getStatus() == RequestStatus.RESOLVED && request.getResolvedAt() == null) {
                request.setResolvedAt(LocalDateTime.now());
            }
        }

        if (dto.getPriority() != null) {
            request.setPriority(dto.getPriority());
        }

        if (dto.getResolutionNotes() != null && !dto.getResolutionNotes().trim().isEmpty()) {
            request.setResolutionNotes(dto.getResolutionNotes().trim());
        }

        ServiceRequest updated = requestRepository.save(request);

        String msg = "Request " + request.getTicketId() + " status updated to " + request.getStatus();
        if (request.getStatus() == RequestStatus.RESOLVED) {
            msg = "Request " + request.getTicketId() + " has been RESOLVED! Please provide your feedback.";
        }

        notificationService.createNotification(request.getUser(), updated, msg);

        return convertToDTO(updated);
    }

    public StatisticsDTO getStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        long total = requestRepository.count();
        stats.setTotalRequests(total);
        stats.setPendingRequests(requestRepository.countByStatus(RequestStatus.PENDING));
        stats.setAssignedRequests(requestRepository.countByStatus(RequestStatus.ASSIGNED));
        stats.setInProgressRequests(requestRepository.countByStatus(RequestStatus.IN_PROGRESS));
        stats.setResolvedRequests(requestRepository.countByStatus(RequestStatus.RESOLVED));
        stats.setClosedRequests(requestRepository.countByStatus(RequestStatus.CLOSED));

        stats.setHighPriorityRequests(requestRepository.countByPriority(Priority.HIGH));
        stats.setUrgentPriorityRequests(requestRepository.countByPriority(Priority.URGENT));

        stats.setTotalUsers(userRepository.count());
        stats.setTotalStaff(staffRepository.count());

        Double avgRating = feedbackRepository.findAverageRating();
        stats.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        stats.setTotalFeedbackCount(feedbackRepository.count());

        Map<String, Long> categoryMap = new HashMap<>();
        for (Category cat : Category.values()) {
            categoryMap.put(cat.name(), 0L);
        }
        for (Object[] row : requestRepository.countRequestsByCategory()) {
            Category cat = (Category) row[0];
            Long count = (Long) row[1];
            categoryMap.put(cat.name(), count);
        }
        stats.setRequestsByCategory(categoryMap);

        Map<String, Long> statusMap = new HashMap<>();
        for (RequestStatus st : RequestStatus.values()) {
            statusMap.put(st.name(), 0L);
        }
        for (Object[] row : requestRepository.countRequestsByStatus()) {
            RequestStatus st = (RequestStatus) row[0];
            Long count = (Long) row[1];
            statusMap.put(st.name(), count);
        }
        stats.setRequestsByStatus(statusMap);

        Map<String, Long> priorityMap = new HashMap<>();
        for (Priority pr : Priority.values()) {
            priorityMap.put(pr.name(), 0L);
        }
        for (Object[] row : requestRepository.countRequestsByPriority()) {
            Priority pr = (Priority) row[0];
            Long count = (Long) row[1];
            priorityMap.put(pr.name(), count);
        }
        stats.setRequestsByPriority(priorityMap);

        return stats;
    }

    public ServiceRequestDTO convertToDTO(ServiceRequest req) {
        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(req.getId());
        dto.setTicketId(req.getTicketId());
        dto.setUserId(req.getUser().getId());
        dto.setUserName(req.getUser().getName());
        dto.setUserEmail(req.getUser().getEmail());
        dto.setTitle(req.getTitle());
        dto.setDescription(req.getDescription());
        dto.setCategory(req.getCategory());
        dto.setPriority(req.getPriority());
        dto.setStatus(req.getStatus());
        dto.setLocation(req.getLocation());

        if (req.getAssignedStaff() != null) {
            dto.setAssignedStaffId(req.getAssignedStaff().getId());
            dto.setAssignedStaffName(req.getAssignedStaff().getName());
            dto.setAssignedStaffDepartment(req.getAssignedStaff().getDepartment().name());
        }

        dto.setResolutionNotes(req.getResolutionNotes());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setUpdatedAt(req.getUpdatedAt());
        dto.setResolvedAt(req.getResolvedAt());
        return dto;
    }
}
