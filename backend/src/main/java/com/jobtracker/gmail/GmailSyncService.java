package com.jobtracker.gmail;

import com.jobtracker.emails.ConnectedEmail;
import com.jobtracker.emails.ConnectedEmailService;
import com.jobtracker.gmail.GmailDtos.GmailSyncRequest;
import com.jobtracker.gmail.GmailDtos.GmailSyncResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GmailSyncService {
    private final ConnectedEmailService connectedEmailService;

    public GmailSyncService(ConnectedEmailService connectedEmailService) {
        this.connectedEmailService = connectedEmailService;
    }

    @Transactional
    public GmailSyncResponse sync(GmailSyncRequest request) {
        ConnectedEmail email = connectedEmailService.findOwned(request.connectedEmailId());
        email.markSynced();
        return new GmailSyncResponse(
                0,
                0,
                "Manual sync endpoint is ready. Gmail API token exchange and readonly scan are the next integration step.");
    }
}
