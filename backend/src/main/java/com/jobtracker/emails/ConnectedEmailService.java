package com.jobtracker.emails;

import com.jobtracker.auth.CurrentUser;
import com.jobtracker.emails.ConnectedEmailDtos.ConnectedEmailRequest;
import com.jobtracker.emails.ConnectedEmailDtos.ConnectedEmailResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConnectedEmailService {
    private final ConnectedEmailRepository repository;
    private final CurrentUser currentUser;

    public ConnectedEmailService(ConnectedEmailRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    public List<ConnectedEmailResponse> list() {
        return repository.findAllByOwnerOrderByConnectedAtDesc(currentUser.get()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConnectedEmailResponse create(ConnectedEmailRequest request) {
        var owner = currentUser.get();
        String emailAddress = request.emailAddress().toLowerCase();
        if (repository.existsByOwnerAndEmailAddress(owner, emailAddress)) {
            throw new IllegalArgumentException("Email is already connected");
        }
        ConnectedEmail email = new ConnectedEmail();
        email.setOwner(owner);
        email.setEmailAddress(emailAddress);
        email.setDisplayName(request.displayName());
        return toResponse(repository.save(email));
    }

    @Transactional
    public ConnectedEmailResponse setActive(UUID id, boolean active) {
        ConnectedEmail email = findOwned(id);
        email.setActive(active);
        return toResponse(email);
    }

    public ConnectedEmail findOwned(UUID id) {
        ConnectedEmail email = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Connected email not found"));
        if (!email.getOwner().getId().equals(currentUser.get().getId())) {
            throw new EntityNotFoundException("Connected email not found");
        }
        return email;
    }

    private ConnectedEmailResponse toResponse(ConnectedEmail email) {
        return new ConnectedEmailResponse(
                email.getId(),
                email.getEmailAddress(),
                email.getDisplayName(),
                email.getProvider(),
                email.isActive(),
                email.getConnectedAt(),
                email.getLastSyncedAt());
    }
}
