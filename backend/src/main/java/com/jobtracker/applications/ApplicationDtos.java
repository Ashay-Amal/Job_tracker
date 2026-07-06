package com.jobtracker.applications;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public final class ApplicationDtos {
    private ApplicationDtos() {
    }

    public record ApplicationRequest(
            @NotBlank String company,
            @NotBlank String role,
            String description,
            String jobLink,
            @NotNull ApplicationStatus status,
            LocalDate appliedDate,
            boolean shortlisted,
            boolean referred,
            String referralSource,
            String referralContact,
            String referralEvidenceLink,
            String referralEvidenceEmailId,
            String notes,
            Set<String> skills,
            Set<String> tags
    ) {
    }

    public record ApplicationResponse(
            UUID id,
            String company,
            String role,
            String description,
            String jobLink,
            ApplicationStatus status,
            LocalDate appliedDate,
            boolean shortlisted,
            boolean referred,
            String referralSource,
            String referralContact,
            String referralEvidenceLink,
            String referralEvidenceEmailId,
            String notes,
            Set<String> skills,
            Set<String> tags,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}
