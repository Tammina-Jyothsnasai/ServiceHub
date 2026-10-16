package com.servicehub.controller;

import com.servicehub.dto.FeedbackDTO;
import com.servicehub.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(originPatterns = "*")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackDTO> submitFeedback(
            @Valid @RequestBody FeedbackDTO dto,
            @RequestParam(name = "userId") Long userId) {
        FeedbackDTO created = feedbackService.submitFeedback(dto, userId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<FeedbackDTO> getFeedbackByRequestId(@PathVariable(name = "requestId") Long requestId) {
        FeedbackDTO dto = feedbackService.getFeedbackByRequestId(requestId);
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }
}
