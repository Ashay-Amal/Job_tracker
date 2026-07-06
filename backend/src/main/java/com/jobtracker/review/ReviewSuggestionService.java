package com.jobtracker.review;

import com.jobtracker.applications.JobApplication;
import com.jobtracker.applications.JobApplicationService;
import com.jobtracker.auth.CurrentUser;
import com.jobtracker.review.ReviewDtos.ReviewSuggestionRequest;
import com.jobtracker.review.ReviewDtos.ReviewSuggestionResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewSuggestionService {
    private final ReviewSuggestionRepository repository;
    private final CurrentUser currentUser;
    private final JobApplicationService applicationService;

    public ReviewSuggestionService(
            ReviewSuggestionRepository repository,
            CurrentUser currentUser,
            JobApplicationService applicationService
    ) {
        this.repository = repository;
        this.currentUser = currentUser;
        this.applicationService = applicationService;
    }

    public List<ReviewSuggestionResponse> pending() {
        return repository.findAllByOwnerAndStateOrderByCreatedAtDesc(currentUser.get(), ReviewState.PENDING).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ReviewSuggestionResponse create(ReviewSuggestionRequest request) {
        ReviewSuggestion suggestion = new ReviewSuggestion();
        suggestion.setOwner(currentUser.get());
        if (request.targetApplicationId() != null) {
            applicationService.findOwned(request.targetApplicationId());
        }
        suggestion.setTargetApplicationId(request.targetApplicationId());
        suggestion.setType(request.type());
        suggestion.setConfidence(request.confidence());
        suggestion.setSummary(request.summary());
        suggestion.setEvidenceSnippet(request.evidenceSnippet());
        suggestion.setEvidenceEmailId(request.evidenceEmailId());
        suggestion.setProposedStatus(request.proposedStatus());
        suggestion.setProposedReferred(request.proposedReferred());
        suggestion.setProposedReferralSource(request.proposedReferralSource());
        suggestion.setProposedReferralContact(request.proposedReferralContact());
        suggestion.setProposedSkills(request.proposedSkills() == null ? Set.of() : request.proposedSkills());
        if (!suggestion.getConfidenceBand().shouldQueue()) {
            suggestion.resolve(ReviewState.REJECTED);
        }
        return toResponse(repository.save(suggestion));
    }

    @Transactional
    public ReviewSuggestionResponse approve(UUID id) {
        ReviewSuggestion suggestion = findOwned(id);
        ensurePending(suggestion);
        if (suggestion.getTargetApplicationId() != null) {
            JobApplication app = applicationService.findOwned(suggestion.getTargetApplicationId());
            if (suggestion.getProposedStatus() != null) {
                app.setStatus(suggestion.getProposedStatus());
            }
            if (suggestion.getProposedReferred() != null) {
                app.setReferred(suggestion.getProposedReferred());
                app.setReferralEvidenceEmailId(suggestion.getEvidenceEmailId());
                app.setReferralSource(suggestion.getProposedReferralSource());
                app.setReferralContact(suggestion.getProposedReferralContact());
            }
            if (!suggestion.getProposedSkills().isEmpty()) {
                app.getSkills().addAll(suggestion.getProposedSkills());
            }
        }
        suggestion.resolve(ReviewState.APPROVED);
        return toResponse(suggestion);
    }

    @Transactional
    public ReviewSuggestionResponse reject(UUID id) {
        ReviewSuggestion suggestion = findOwned(id);
        ensurePending(suggestion);
        suggestion.resolve(ReviewState.REJECTED);
        return toResponse(suggestion);
    }

    private void ensurePending(ReviewSuggestion suggestion) {
        if (suggestion.getState() != ReviewState.PENDING) {
            throw new IllegalArgumentException("Review suggestion is already resolved");
        }
    }

    private ReviewSuggestion findOwned(UUID id) {
        ReviewSuggestion suggestion = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review suggestion not found"));
        if (!suggestion.getOwner().getId().equals(currentUser.get().getId())) {
            throw new EntityNotFoundException("Review suggestion not found");
        }
        return suggestion;
    }

    private ReviewSuggestionResponse toResponse(ReviewSuggestion suggestion) {
        return new ReviewSuggestionResponse(
                suggestion.getId(),
                suggestion.getTargetApplicationId(),
                suggestion.getType(),
                suggestion.getState(),
                suggestion.getConfidenceBand(),
                suggestion.getConfidence(),
                suggestion.getSummary(),
                suggestion.getEvidenceSnippet(),
                suggestion.getEvidenceEmailId(),
                suggestion.getProposedStatus(),
                suggestion.getProposedReferred(),
                suggestion.getProposedReferralSource(),
                suggestion.getProposedReferralContact(),
                suggestion.getProposedSkills(),
                suggestion.getCreatedAt(),
                suggestion.getResolvedAt());
    }
}
