package com.jobtracker.emails;

import com.jobtracker.users.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "connected_emails",
        uniqueConstraints = @UniqueConstraint(name = "uk_connected_email_owner_address", columnNames = {"owner_id", "email_address"})
)
public class ConnectedEmail {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserAccount owner;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    private String displayName;

    @Column(nullable = false)
    private String provider = "GMAIL";

    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 2048)
    private String encryptedRefreshToken;

    @Column(nullable = false, updatable = false)
    private Instant connectedAt;

    private Instant lastSyncedAt;

    @PrePersist
    void prePersist() {
        connectedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress == null ? null : emailAddress.toLowerCase();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProvider() {
        return provider;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEncryptedRefreshToken() {
        return encryptedRefreshToken;
    }

    public void setEncryptedRefreshToken(String encryptedRefreshToken) {
        this.encryptedRefreshToken = encryptedRefreshToken;
    }

    public Instant getConnectedAt() {
        return connectedAt;
    }

    public Instant getLastSyncedAt() {
        return lastSyncedAt;
    }

    public void markSynced() {
        this.lastSyncedAt = Instant.now();
    }
}
