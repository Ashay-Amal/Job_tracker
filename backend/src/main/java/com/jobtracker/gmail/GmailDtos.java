package com.jobtracker.gmail;

import java.util.UUID;

public final class GmailDtos {
    private GmailDtos() {
    }

    public record GmailSyncRequest(UUID connectedEmailId) {
    }

    public record GmailSyncResponse(
            int scannedMessages,
            int suggestionsCreated,
            String message
    ) {
    }
}
