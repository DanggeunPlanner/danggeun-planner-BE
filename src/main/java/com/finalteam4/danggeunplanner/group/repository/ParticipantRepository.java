package com.finalteam4.danggeunplanner.group.repository;

import com.finalteam4.danggeunplanner.group.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Modifying
    @Query("delete from Participant p where p.group.id = :groupId")
    void deleteAllByGroupId(@Param("groupId") Long groupId);
}
