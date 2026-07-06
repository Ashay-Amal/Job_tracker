package com.jobtracker.applications;

import com.jobtracker.users.UserAccount;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "job_applications")
public class JobApplication {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserAccount owner;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String role;

    @Column(length = 4096)
    private String description;

    @Column(length = 1024)
    private String jobLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private LocalDate appliedDate;

    @Column(nullable = false)
    private boolean shortlisted;

    @Column(nullable = false)
    private boolean referred;

    private String referralSource;
    private String referralContact;
    private String referralEvidenceLink;
    private String referralEvidenceEmailId;

    @Column(length = 2048)
    private String notes;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "application_skills", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "skill")
    private Set<String> skills = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "application_tags", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "tag")
    private Set<String> tags = new LinkedHashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public boolean isShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(boolean shortlisted) {
        this.shortlisted = shortlisted;
    }

    public boolean isReferred() {
        return referred;
    }

    public void setReferred(boolean referred) {
        this.referred = referred;
    }

    public String getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(String referralSource) {
        this.referralSource = referralSource;
    }

    public String getReferralContact() {
        return referralContact;
    }

    public void setReferralContact(String referralContact) {
        this.referralContact = referralContact;
    }

    public String getReferralEvidenceLink() {
        return referralEvidenceLink;
    }

    public void setReferralEvidenceLink(String referralEvidenceLink) {
        this.referralEvidenceLink = referralEvidenceLink;
    }

    public String getReferralEvidenceEmailId() {
        return referralEvidenceEmailId;
    }

    public void setReferralEvidenceEmailId(String referralEvidenceEmailId) {
        this.referralEvidenceEmailId = referralEvidenceEmailId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills == null ? new LinkedHashSet<>() : new LinkedHashSet<>(skills);
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags == null ? new LinkedHashSet<>() : new LinkedHashSet<>(tags);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
