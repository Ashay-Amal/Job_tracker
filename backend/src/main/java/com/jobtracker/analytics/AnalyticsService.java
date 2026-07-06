package com.jobtracker.analytics;

import com.jobtracker.analytics.AnalyticsDtos.AnalyticsResponse;
import com.jobtracker.applications.ApplicationFilter;
import com.jobtracker.applications.ApplicationStatus;
import com.jobtracker.applications.JobApplication;
import com.jobtracker.applications.JobApplicationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private final JobApplicationService applicationService;

    public AnalyticsService(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public AnalyticsResponse analytics(ApplicationFilter filter) {
        List<JobApplication> apps = applicationService.filterForCurrentUser(filter);
        long total = apps.size();
        long applied = apps.stream().filter(app -> app.getStatus() != ApplicationStatus.SAVED).count();
        long shortlisted = apps.stream().filter(JobApplication::isShortlisted).count();
        long oa = apps.stream().filter(app -> app.getStatus() == ApplicationStatus.OA).count();
        long interview = apps.stream().filter(app -> app.getStatus() == ApplicationStatus.INTERVIEW).count();
        long offer = apps.stream().filter(app -> app.getStatus() == ApplicationStatus.OFFER).count();
        long rejected = apps.stream().filter(app -> app.getStatus() == ApplicationStatus.REJECTED).count();
        long referred = apps.stream().filter(JobApplication::isReferred).count();
        return new AnalyticsResponse(
                total,
                applied,
                shortlisted,
                rate(shortlisted, total),
                oa,
                rate(oa, total),
                interview,
                rate(interview, total),
                offer,
                rejected,
                referred);
    }

    private double rate(long numerator, long denominator) {
        if (denominator == 0) {
            return 0;
        }
        return Math.round((numerator * 10000.0 / denominator)) / 100.0;
    }
}
