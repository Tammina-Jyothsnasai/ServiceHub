package com.servicehub.service;

import com.servicehub.dto.FeedbackDTO;
import com.servicehub.entity.Feedback;
import com.servicehub.entity.ServiceRequest;
import com.servicehub.entity.User;
import com.servicehub.enums.RequestStatus;
import com.servicehub.exception.BadRequestException;
import com.servicehub.exception.ResourceNotFoundException;
import com.servicehub.repository.FeedbackRepository;
import com.servicehub.repository.ServiceRequestRepository;
import com.servicehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ServiceRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository,
                            ServiceRequestRepository requestRepository,
                            UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    public FeedbackDTO submitFeedback(FeedbackDTO dto, Long userId) {
        ServiceRequest request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));

        if (request.getStatus() != RequestStatus.RESOLVED && request.getStatus() != RequestStatus.CLOSED) {
            throw new BadRequestException("Feedback can only be submitted for RESOLVED or CLOSED requests");
        }

        if (!request.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only submit feedback for your own requests");
        }

        if (feedbackRepository.existsByServiceRequestId(dto.getRequestId())) {
            throw new BadRequestException("Feedback has already been submitted for this request");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Feedback feedback = new Feedback();
        feedback.setServiceRequest(request);
        feedback.setUser(user);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment() != null ? dto.getComment().trim() : "");

        Feedback saved = feedbackRepository.save(feedback);
        return convertToDTO(saved);
    }

    public List<FeedbackDTO> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FeedbackDTO getFeedbackByRequestId(Long requestId) {
        Feedback feedback = feedbackRepository.findByServiceRequestId(requestId)
                .orElse(null);
        return feedback != null ? convertToDTO(feedback) : null;
    }

    public FeedbackDTO convertToDTO(Feedback fb) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(fb.getId());
        dto.setRequestId(fb.getServiceRequest().getId());
        dto.setRequestTicketId(fb.getServiceRequest().getTicketId());
        dto.setRequestTitle(fb.getServiceRequest().getTitle());
        dto.setUserId(fb.getUser().getId());
        dto.setUserName(fb.getUser().getName());
        dto.setRating(fb.getRating());
        dto.setComment(fb.getComment());
        dto.setCreatedAt(fb.getCreatedAt());
        return dto;
    }
}
