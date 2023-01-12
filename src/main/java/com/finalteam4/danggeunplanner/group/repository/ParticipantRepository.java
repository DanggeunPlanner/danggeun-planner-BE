package com.finalteam4.danggeunplanner.group.repository;

import com.finalteam4.danggeunplanner.group.entity.Participant;
import com.finalteam4.danggeunplanner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Transactional
    @Modifying
    @Query("delete from Participant p where p.group.id = :groupId")
    void deleteAllByGroupId(@Param("groupId") Long groupId);
    List<Participant> findAllByMember(Member member);
    Integer countParticipantByGroup_Id(Long groupId);
}
