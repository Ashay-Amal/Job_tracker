package com.jobtracker.review;

import com.jobtracker.users.UserAccount;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSuggestionRepository extends JpaRepository<ReviewSuggestion, UUID> {
    List<ReviewSuggestion> findAllByOwnerAndStateOrderByCreatedAtDesc(UserAccount owner, ReviewState state);
}
