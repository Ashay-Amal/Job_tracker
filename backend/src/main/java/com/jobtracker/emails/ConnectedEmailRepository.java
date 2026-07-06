package com.jobtracker.emails;

import com.jobtracker.users.UserAccount;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectedEmailRepository extends JpaRepository<ConnectedEmail, UUID> {
    List<ConnectedEmail> findAllByOwnerOrderByConnectedAtDesc(UserAccount owner);

    boolean existsByOwnerAndEmailAddress(UserAccount owner, String emailAddress);
}
