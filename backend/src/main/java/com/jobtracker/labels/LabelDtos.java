package com.jobtracker.labels;

import java.util.Set;

public final class LabelDtos {
    private LabelDtos() {
    }

    public record ExtractLabelsRequest(String text) {
    }

    public record ExtractLabelsResponse(Set<String> labels, boolean aiAvailable, String source) {
    }
}
