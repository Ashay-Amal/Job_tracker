package com.jobtracker.savedjobs;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public final class SavedJobDtos {
    private SavedJobDtos() {
    }

    public record SavedJobRequest(
            @NotBlank String company,
            @NotBlank String role,
            String jobLink,
            String description,
            String notes,
            Set<String> skills,
            Set<String> tags
    ) {
    }

    public record SavedJobResponse(
            UUID id,
            String company,
            String role,
            String jobLink,
            String description,
            String notes,
            Set<String> skills,
            Set<String> tags,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}
