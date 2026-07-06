package com.jobtracker.applications;

import com.jobtracker.users.UserAccount;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    List<JobApplication> findAllByOwnerOrderByCreatedAtDesc(UserAccount owner);
}
