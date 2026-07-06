package com.jobtracker.ai;

import com.jobtracker.config.AppProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OllamaClient {
    private final AppProperties appProperties;

    public OllamaClient(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public boolean isConfigured() {
        return StringUtils.hasText(appProperties.getOllamaBaseUrl());
    }
}
