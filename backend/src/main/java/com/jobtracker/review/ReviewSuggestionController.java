package com.jobtracker.review;

import com.jobtracker.review.ReviewDtos.ReviewSuggestionRequest;
import com.jobtracker.review.ReviewDtos.ReviewSuggestionResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review-suggestions")
public class ReviewSuggestionController {
    private final ReviewSuggestionService service;

    public ReviewSuggestionController(ReviewSuggestionService service) {
        this.service = service;
    }

    @GetMapping
    List<ReviewSuggestionResponse> pending() {
        return service.pending();
    }

    @PostMapping
    ReviewSuggestionResponse create(@Valid @RequestBody ReviewSuggestionRequest request) {
        return service.create(request);
    }

    @PostMapping("/{id}/approve")
    ReviewSuggestionResponse approve(@PathVariable UUID id) {
        return service.approve(id);
    }

    @PostMapping("/{id}/reject")
    ReviewSuggestionResponse reject(@PathVariable UUID id) {
        return service.reject(id);
    }
}
