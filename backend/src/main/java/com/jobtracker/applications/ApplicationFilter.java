package com.jobtracker.applications;

public record ApplicationFilter(
        Integer month,
        Integer year,
        String company,
        String role,
        String skill,
        String tag,
        ApplicationStatus status,
        Boolean referred,
        Boolean shortlisted
) {
}
