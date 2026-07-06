package com.jobtracker.emails;

import com.jobtracker.emails.ConnectedEmailDtos.ConnectedEmailRequest;
import com.jobtracker.emails.ConnectedEmailDtos.ConnectedEmailResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/connected-emails")
public class ConnectedEmailController {
    private final ConnectedEmailService service;

    public ConnectedEmailController(ConnectedEmailService service) {
        this.service = service;
    }

    @GetMapping
    List<ConnectedEmailResponse> list() {
        return service.list();
    }

    @PostMapping
    ConnectedEmailResponse create(@Valid @RequestBody ConnectedEmailRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/active")
    ConnectedEmailResponse setActive(@PathVariable UUID id, @RequestParam boolean active) {
        return service.setActive(id, active);
    }
}
