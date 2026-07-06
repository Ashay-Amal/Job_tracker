package com.jobtracker.applications;

import com.jobtracker.applications.ApplicationDtos.ApplicationRequest;
import com.jobtracker.applications.ApplicationDtos.ApplicationResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {
    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    @GetMapping
    List<ApplicationResponse> list(
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
        return service.list(new ApplicationFilter(month, year, company, role, skill, tag, status, referred, shortlisted));
    }

    @GetMapping("/{id}")
    ApplicationResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    ApplicationResponse create(@Valid @RequestBody ApplicationRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    ApplicationResponse update(@PathVariable UUID id, @Valid @RequestBody ApplicationRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
