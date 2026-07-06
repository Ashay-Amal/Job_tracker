package com.jobtracker.labels;

import com.jobtracker.labels.LabelDtos.ExtractLabelsRequest;
import com.jobtracker.labels.LabelDtos.ExtractLabelsResponse;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    Set<String> labels() {
        return labelService.allLabels();
    }

    @PostMapping("/extract")
    ExtractLabelsResponse extract(@RequestBody ExtractLabelsRequest request) {
        return labelService.extract(request.text());
    }
}
