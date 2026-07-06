package com.jobtracker.labels;

import com.jobtracker.ai.OllamaClient;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class LabelService {
    private static final Map<String, Set<String>> TAXONOMY = Map.ofEntries(
            Map.entry("react", Set.of("react", "react.js", "reactjs")),
            Map.entry("spring boot", Set.of("spring boot", "springboot")),
            Map.entry("java", Set.of("java")),
            Map.entry("typescript", Set.of("typescript", "type script")),
            Map.entry("postgresql", Set.of("postgresql", "postgres")),
            Map.entry("redis", Set.of("redis")),
            Map.entry("aws", Set.of("aws", "amazon web services")),
            Map.entry("docker", Set.of("docker", "container")),
            Map.entry("kubernetes", Set.of("kubernetes", "k8s")),
            Map.entry("python", Set.of("python")),
            Map.entry("machine learning", Set.of("machine learning", "ml")),
            Map.entry("llm", Set.of("llm", "large language model", "generative ai")),
            Map.entry("sql", Set.of("sql")),
            Map.entry("graphql", Set.of("graphql"))
    );

    private final OllamaClient ollamaClient;

    public LabelService(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    public Set<String> allLabels() {
        return TAXONOMY.keySet();
    }

    public LabelDtos.ExtractLabelsResponse extract(String text) {
        Set<String> labels = extractDeterministic(text);
        return new LabelDtos.ExtractLabelsResponse(labels, ollamaClient.isConfigured(), "taxonomy");
    }

    public Set<String> extractDeterministic(String text) {
        String value = text == null ? "" : text.toLowerCase(Locale.ROOT);
        Set<String> labels = new LinkedHashSet<>();
        TAXONOMY.forEach((normalized, aliases) -> {
            if (aliases.stream().anyMatch(value::contains)) {
                labels.add(normalized);
            }
        });
        return labels;
    }
}
