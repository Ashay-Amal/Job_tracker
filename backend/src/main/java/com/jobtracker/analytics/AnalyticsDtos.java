package com.jobtracker.analytics;

public final class AnalyticsDtos {
    private AnalyticsDtos() {
    }

    public record AnalyticsResponse(
            long totalCount,
            long appliedCount,
            long shortlistedCount,
            double shortlistedRate,
            long oaCount,
            double oaRate,
            long interviewCount,
            double interviewRate,
            long offerCount,
            long rejectedCount,
            long referredCount
    ) {
    }
}
