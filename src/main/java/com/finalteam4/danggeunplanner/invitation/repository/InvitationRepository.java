package com.finalteam4.danggeunplanner.invitation.repository;

import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.invitation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Boolean existsByGroup(Group group);

    Invitation findByGroup(Group group);
}
