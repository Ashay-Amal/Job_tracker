package com.jobtracker.applications;

import com.jobtracker.applications.ApplicationDtos.ApplicationRequest;
import com.jobtracker.applications.ApplicationDtos.ApplicationResponse;
import com.jobtracker.auth.CurrentUser;
import com.jobtracker.users.UserAccount;
import jakarta.persistence.EntityNotFoundException;
import java.time.Month;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobApplicationService {
    private final JobApplicationRepository repository;
    private final CurrentUser currentUser;

    public JobApplicationService(JobApplicationRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    public List<ApplicationResponse> list(ApplicationFilter filter) {
        UserAccount owner = currentUser.get();
        return filter(repository.findAllByOwnerOrderByCreatedAtDesc(owner), filter).stream()
                .map(JobApplicationMapper::toResponse)
                .toList();
    }

    public List<JobApplication> filterForCurrentUser(ApplicationFilter filter) {
        return filter(repository.findAllByOwnerOrderByCreatedAtDesc(currentUser.get()), filter);
    }

    public ApplicationResponse get(UUID id) {
        return JobApplicationMapper.toResponse(findOwned(id));
    }

    @Transactional
    public ApplicationResponse create(ApplicationRequest request) {
        JobApplication app = new JobApplication();
        app.setOwner(currentUser.get());
        JobApplicationMapper.apply(app, request);
        return JobApplicationMapper.toResponse(repository.save(app));
    }

    @Transactional
    public ApplicationResponse update(UUID id, ApplicationRequest request) {
        JobApplication app = findOwned(id);
        JobApplicationMapper.apply(app, request);
        return JobApplicationMapper.toResponse(app);
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(findOwned(id));
    }

    public JobApplication findOwned(UUID id) {
        JobApplication app = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        if (!app.getOwner().getId().equals(currentUser.get().getId())) {
            throw new EntityNotFoundException("Application not found");
        }
        return app;
    }

    private List<JobApplication> filter(List<JobApplication> apps, ApplicationFilter filter) {
        return apps.stream()
                .filter(app -> filter.status() == null || app.getStatus() == filter.status())
                .filter(app -> filter.referred() == null || app.isReferred() == filter.referred())
                .filter(app -> filter.shortlisted() == null || app.isShortlisted() == filter.shortlisted())
                .filter(app -> filter.year() == null || (app.getAppliedDate() != null && app.getAppliedDate().getYear() == filter.year()))
                .filter(app -> filter.month() == null || matchesMonth(app, filter.month()))
                .filter(app -> contains(app.getCompany(), filter.company()))
                .filter(app -> contains(app.getRole(), filter.role()))
                .filter(app -> filter.skill() == null || app.getSkills().stream().anyMatch(skill -> contains(skill, filter.skill())))
                .filter(app -> filter.tag() == null || app.getTags().stream().anyMatch(tag -> contains(tag, filter.tag())))
                .toList();
    }

    private boolean contains(String value, String needle) {
        if (needle == null || needle.isBlank()) {
            return true;
        }
        return value != null && value.toLowerCase(Locale.ROOT).contains(needle.toLowerCase(Locale.ROOT));
    }

    private boolean matchesMonth(JobApplication app, int month) {
        return month >= 1
                && month <= 12
                && app.getAppliedDate() != null
                && app.getAppliedDate().getMonth() == Month.of(month);
    }
}
