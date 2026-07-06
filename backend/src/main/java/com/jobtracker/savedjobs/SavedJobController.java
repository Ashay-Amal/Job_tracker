package com.jobtracker.savedjobs;

import com.jobtracker.savedjobs.SavedJobDtos.SavedJobRequest;
import com.jobtracker.savedjobs.SavedJobDtos.SavedJobResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {
    private final SavedJobService service;

    public SavedJobController(SavedJobService service) {
        this.service = service;
    }

    @GetMapping
    List<SavedJobResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    SavedJobResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    SavedJobResponse create(@Valid @RequestBody SavedJobRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    SavedJobResponse update(@PathVariable UUID id, @Valid @RequestBody SavedJobRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
