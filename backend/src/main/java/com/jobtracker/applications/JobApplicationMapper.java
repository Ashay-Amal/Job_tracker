package com.jobtracker.applications;

import com.jobtracker.applications.ApplicationDtos.ApplicationRequest;
import com.jobtracker.applications.ApplicationDtos.ApplicationResponse;

public final class JobApplicationMapper {
    private JobApplicationMapper() {
    }

    public static ApplicationResponse toResponse(JobApplication app) {
        return new ApplicationResponse(
                app.getId(),
                app.getCompany(),
                app.getRole(),
                app.getDescription(),
                app.getJobLink(),
                app.getStatus(),
                app.getAppliedDate(),
                app.isShortlisted(),
                app.isReferred(),
                app.getReferralSource(),
                app.getReferralContact(),
                app.getReferralEvidenceLink(),
                app.getReferralEvidenceEmailId(),
                app.getNotes(),
                app.getSkills(),
                app.getTags(),
                app.getCreatedAt(),
                app.getUpdatedAt());
    }

    public static void apply(JobApplication app, ApplicationRequest request) {
        app.setCompany(request.company());
        app.setRole(request.role());
        app.setDescription(request.description());
        app.setJobLink(request.jobLink());
        app.setStatus(request.status());
        app.setAppliedDate(request.appliedDate());
        app.setShortlisted(request.shortlisted());
        app.setReferred(request.referred());
        app.setReferralSource(request.referralSource());
        app.setReferralContact(request.referralContact());
        app.setReferralEvidenceLink(request.referralEvidenceLink());
        app.setReferralEvidenceEmailId(request.referralEvidenceEmailId());
        app.setNotes(request.notes());
        app.setSkills(request.skills());
        app.setTags(request.tags());
    }
}
