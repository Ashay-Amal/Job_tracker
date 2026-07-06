package com.jobtracker.review;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConfidenceBandTest {
    @Test
    void mapsBalancedThresholds() {
        assertThat(ConfidenceBand.from(0.59)).isEqualTo(ConfidenceBand.LOW);
        assertThat(ConfidenceBand.from(0.60)).isEqualTo(ConfidenceBand.MEDIUM);
        assertThat(ConfidenceBand.from(0.79)).isEqualTo(ConfidenceBand.MEDIUM);
        assertThat(ConfidenceBand.from(0.80)).isEqualTo(ConfidenceBand.HIGH);
    }
}
