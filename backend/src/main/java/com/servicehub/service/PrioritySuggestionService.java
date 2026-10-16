package com.servicehub.service;

import com.servicehub.dto.PrioritySuggestResponse;
import com.servicehub.enums.Priority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class PrioritySuggestionService {

    private static final List<String> URGENT_KEYWORDS = Arrays.asList(
            "fire", "emergency", "dangerous", "accident", "explosion", "hazard", "gas leak", "smoke", "short circuit"
    );

    private static final List<String> HIGH_KEYWORDS = Arrays.asList(
            "completely down", "not working", "server down", "no internet", "security", "broken pipe",
            "overflow", "outage", "blackout", "urgent", "critical", "stopped working"
    );

    private static final List<String> MEDIUM_KEYWORDS = Arrays.asList(
            "issue", "problem", "damaged", "slow", "leaking", "glitch", "malfunction", "noise",
            "faulty", "repair", "breakdown"
    );

    private static final List<String> LOW_KEYWORDS = Arrays.asList(
            "minor", "small", "cosmetic", "paint", "light bulb", "loose screw", "crack", "dust", "cleaning needed"
    );

    public PrioritySuggestResponse suggestPriority(String description) {
        if (description == null || description.trim().isEmpty()) {
            return new PrioritySuggestResponse(Priority.MEDIUM, "Default priority for unassigned description.");
        }

        String lowerText = description.toLowerCase(Locale.ROOT);

        for (String keyword : URGENT_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                return new PrioritySuggestResponse(Priority.URGENT, "Detected critical emergency keyword: '" + keyword + "'");
            }
        }

        for (String keyword : HIGH_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                return new PrioritySuggestResponse(Priority.HIGH, "Detected high priority impact keyword: '" + keyword + "'");
            }
        }

        for (String keyword : MEDIUM_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                return new PrioritySuggestResponse(Priority.MEDIUM, "Detected standard service issue keyword: '" + keyword + "'");
            }
        }

        for (String keyword : LOW_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                return new PrioritySuggestResponse(Priority.LOW, "Detected minor/cosmetic keyword: '" + keyword + "'");
            }
        }

        return new PrioritySuggestResponse(Priority.MEDIUM, "Standard default priority applied.");
    }
}
