package com.jobtracker.review;

import com.jobtracker.applications.ApplicationStatus;
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
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "review_suggestions")
public class ReviewSuggestion {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserAccount owner;

    private UUID targetApplicationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewState state = ReviewState.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConfidenceBand confidenceBand;

    @Column(nullable = false)
    private double confidence;

    @Column(length = 2048)
    private String summary;

    @Column(length = 2048)
    private String evidenceSnippet;

    private String evidenceEmailId;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus proposedStatus;

    private Boolean proposedReferred;
    private String proposedReferralSource;
    private String proposedReferralContact;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "review_suggestion_skills", joinColumns = @JoinColumn(name = "review_suggestion_id"))
    @Column(name = "skill")
    private Set<String> proposedSkills = new LinkedHashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant resolvedAt;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
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

    public UUID getTargetApplicationId() {
        return targetApplicationId;
    }

    public void setTargetApplicationId(UUID targetApplicationId) {
        this.targetApplicationId = targetApplicationId;
    }

    public SuggestionType getType() {
        return type;
    }

    public void setType(SuggestionType type) {
        this.type = type;
    }

    public ReviewState getState() {
        return state;
    }

    public void setState(ReviewState state) {
        this.state = state;
    }

    public ConfidenceBand getConfidenceBand() {
        return confidenceBand;
    }

    public void setConfidenceBand(ConfidenceBand confidenceBand) {
        this.confidenceBand = confidenceBand;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
        this.confidenceBand = ConfidenceBand.from(confidence);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEvidenceSnippet() {
        return evidenceSnippet;
    }

    public void setEvidenceSnippet(String evidenceSnippet) {
        this.evidenceSnippet = evidenceSnippet;
    }

    public String getEvidenceEmailId() {
        return evidenceEmailId;
    }

    public void setEvidenceEmailId(String evidenceEmailId) {
        this.evidenceEmailId = evidenceEmailId;
    }

    public ApplicationStatus getProposedStatus() {
        return proposedStatus;
    }

    public void setProposedStatus(ApplicationStatus proposedStatus) {
        this.proposedStatus = proposedStatus;
    }

    public Boolean getProposedReferred() {
        return proposedReferred;
    }

    public void setProposedReferred(Boolean proposedReferred) {
        this.proposedReferred = proposedReferred;
    }

    public String getProposedReferralSource() {
        return proposedReferralSource;
    }

    public void setProposedReferralSource(String proposedReferralSource) {
        this.proposedReferralSource = proposedReferralSource;
    }

    public String getProposedReferralContact() {
        return proposedReferralContact;
    }

    public void setProposedReferralContact(String proposedReferralContact) {
        this.proposedReferralContact = proposedReferralContact;
    }

    public Set<String> getProposedSkills() {
        return proposedSkills;
    }

    public void setProposedSkills(Set<String> proposedSkills) {
        this.proposedSkills = proposedSkills == null ? new LinkedHashSet<>() : new LinkedHashSet<>(proposedSkills);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void resolve(ReviewState state) {
        this.state = state;
        this.resolvedAt = Instant.now();
    }
}
