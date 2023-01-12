package com.finalteam4.danggeunplanner.invitation.repository;

import com.finalteam4.danggeunplanner.invitation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
