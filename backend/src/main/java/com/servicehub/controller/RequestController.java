package com.servicehub.controller;

import com.servicehub.dto.*;
import com.servicehub.service.PrioritySuggestionService;
import com.servicehub.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(originPatterns = "*")
public class RequestController {

    private final RequestService requestService;
    private final PrioritySuggestionService prioritySuggestionService;

    @Autowired
    public RequestController(RequestService requestService, PrioritySuggestionService prioritySuggestionService) {
        this.requestService = requestService;
        this.prioritySuggestionService = prioritySuggestionService;
    }

    @PostMapping
    public ResponseEntity<ServiceRequestDTO> createRequest(
            @Valid @RequestBody CreateRequestDTO dto,
            @RequestParam(name = "userId") Long userId) {
        ServiceRequestDTO created = requestService.createRequest(dto, userId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ServiceRequestDTO>> getUserRequests(@PathVariable(name = "userId") Long userId) {
        List<ServiceRequestDTO> list = requestService.getUserRequests(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestDTO> getRequestById(@PathVariable(name = "id") Long id) {
        ServiceRequestDTO dto = requestService.getRequestById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ServiceRequestDTO> getRequestByTicketId(@PathVariable(name = "ticketId") String ticketId) {
        ServiceRequestDTO dto = requestService.getRequestByTicketId(ticketId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/suggest-priority")
    public ResponseEntity<PrioritySuggestResponse> suggestPriority(@RequestBody PrioritySuggestRequest request) {
        PrioritySuggestResponse response = prioritySuggestionService.suggestPriority(request.getDescription());
        return ResponseEntity.ok(response);
    }
}
