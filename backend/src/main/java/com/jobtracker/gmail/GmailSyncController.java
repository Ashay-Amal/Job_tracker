package com.jobtracker.gmail;

import com.jobtracker.gmail.GmailDtos.GmailSyncRequest;
import com.jobtracker.gmail.GmailDtos.GmailSyncResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gmail")
public class GmailSyncController {
    private final GmailSyncService service;

    public GmailSyncController(GmailSyncService service) {
        this.service = service;
    }

    @PostMapping("/sync")
    GmailSyncResponse sync(@Valid @RequestBody GmailSyncRequest request) {
        return service.sync(request);
    }
}
