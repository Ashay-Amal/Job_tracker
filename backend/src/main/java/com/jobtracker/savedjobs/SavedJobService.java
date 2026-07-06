package com.jobtracker.savedjobs;

import com.jobtracker.auth.CurrentUser;
import com.jobtracker.savedjobs.SavedJobDtos.SavedJobRequest;
import com.jobtracker.savedjobs.SavedJobDtos.SavedJobResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SavedJobService {
    private final SavedJobRepository repository;
    private final CurrentUser currentUser;

    public SavedJobService(SavedJobRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    public List<SavedJobResponse> list() {
        return repository.findAllByOwnerOrderByCreatedAtDesc(currentUser.get()).stream()
                .map(this::toResponse)
                .toList();
    }

    public SavedJobResponse get(UUID id) {
        return toResponse(findOwned(id));
    }

    @Transactional
    public SavedJobResponse create(SavedJobRequest request) {
        SavedJob savedJob = new SavedJob();
        savedJob.setOwner(currentUser.get());
        apply(savedJob, request);
        return toResponse(repository.save(savedJob));
    }

    @Transactional
    public SavedJobResponse update(UUID id, SavedJobRequest request) {
        SavedJob savedJob = findOwned(id);
        apply(savedJob, request);
        return toResponse(savedJob);
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(findOwned(id));
    }

    private SavedJob findOwned(UUID id) {
        SavedJob savedJob = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Saved job not found"));
        if (!savedJob.getOwner().getId().equals(currentUser.get().getId())) {
            throw new EntityNotFoundException("Saved job not found");
        }
        return savedJob;
    }

    private void apply(SavedJob savedJob, SavedJobRequest request) {
        savedJob.setCompany(request.company());
        savedJob.setRole(request.role());
        savedJob.setJobLink(request.jobLink());
        savedJob.setDescription(request.description());
        savedJob.setNotes(request.notes());
        savedJob.setSkills(request.skills());
        savedJob.setTags(request.tags());
    }

    private SavedJobResponse toResponse(SavedJob savedJob) {
        return new SavedJobResponse(
                savedJob.getId(),
                savedJob.getCompany(),
                savedJob.getRole(),
                savedJob.getJobLink(),
                savedJob.getDescription(),
                savedJob.getNotes(),
                savedJob.getSkills(),
                savedJob.getTags(),
                savedJob.getCreatedAt(),
                savedJob.getUpdatedAt());
    }
}
