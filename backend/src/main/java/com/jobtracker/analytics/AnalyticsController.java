package com.jobtracker.analytics;

import com.jobtracker.analytics.AnalyticsDtos.AnalyticsResponse;
import com.jobtracker.applications.ApplicationFilter;
import com.jobtracker.applications.ApplicationStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @GetMapping
    AnalyticsResponse analytics(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) Boolean referred,
            @RequestParam(required = false) Boolean shortlisted
    ) {
        return service.analytics(new ApplicationFilter(month, year, company, role, skill, tag, status, referred, shortlisted));
    }
}
