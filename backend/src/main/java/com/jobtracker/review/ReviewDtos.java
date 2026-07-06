package com.jobtracker.review;

import com.jobtracker.applications.ApplicationStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public final class ReviewDtos {
    private ReviewDtos() {
    }

    public record ReviewSuggestionRequest(
            UUID targetApplicationId,
            @NotNull
            SuggestionType type,
            @DecimalMin("0.0")
            @DecimalMax("1.0")
            double confidence,
            String summary,
            String evidenceSnippet,
            String evidenceEmailId,
            ApplicationStatus proposedStatus,
            Boolean proposedReferred,
            String proposedReferralSource,
            String proposedReferralContact,
            Set<String> proposedSkills
    ) {
    }

    public record ReviewSuggestionResponse(
            UUID id,
            UUID targetApplicationId,
            SuggestionType type,
            ReviewState state,
            ConfidenceBand confidenceBand,
            double confidence,
            String summary,
            String evidenceSnippet,
            String evidenceEmailId,
            ApplicationStatus proposedStatus,
            Boolean proposedReferred,
            String proposedReferralSource,
            String proposedReferralContact,
            Set<String> proposedSkills,
            Instant createdAt,
            Instant resolvedAt
    ) {
    }
}
