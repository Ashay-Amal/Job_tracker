package com.jobtracker.labels;

import static org.assertj.core.api.Assertions.assertThat;

import com.jobtracker.ai.OllamaClient;
import com.jobtracker.config.AppProperties;
import org.junit.jupiter.api.Test;

class LabelServiceTest {
    @Test
    void extractsNormalizedLabelsFromDescription() {
        LabelService service = new LabelService(new OllamaClient(new AppProperties()));
        assertThat(service.extractDeterministic("React, SpringBoot, Postgres and Docker experience"))
                .contains("postgresql", "spring boot", "react", "docker");
    }
}
