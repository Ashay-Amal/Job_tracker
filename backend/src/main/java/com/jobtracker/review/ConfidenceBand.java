package com.jobtracker.review;

public enum ConfidenceBand {
    LOW,
    MEDIUM,
    HIGH;

    public static ConfidenceBand from(double confidence) {
        if (confidence >= 0.80) {
            return HIGH;
        }
        if (confidence >= 0.60) {
            return MEDIUM;
        }
        return LOW;
    }

    public boolean shouldQueue() {
        return this == MEDIUM || this == HIGH;
    }
}
