package com.jobtracker.savedjobs;

import com.jobtracker.users.UserAccount;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedJobRepository extends JpaRepository<SavedJob, UUID> {
    List<SavedJob> findAllByOwnerOrderByCreatedAtDesc(UserAccount owner);
}
