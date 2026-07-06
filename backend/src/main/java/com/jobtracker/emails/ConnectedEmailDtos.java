package com.jobtracker.emails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public final class ConnectedEmailDtos {
    private ConnectedEmailDtos() {
    }

    public record ConnectedEmailRequest(
            @Email @NotBlank String emailAddress,
            String displayName
    ) {
    }

    public record ConnectedEmailResponse(
            UUID id,
            String emailAddress,
            String displayName,
            String provider,
            boolean active,
            Instant connectedAt,
            Instant lastSyncedAt
    ) {
    }
}
