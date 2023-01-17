package com.finalteam4.danggeunplanner.participant.repository;

import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Transactional
    @Modifying
    @Query("delete from Participant p where p.group.id = :groupId")
    void deleteAllByGroupId(@Param("groupId") Long groupId);
    List<Participant> findAllByMember(Member member);
    Integer countParticipantByGroup_Id(Long groupId);
    Optional<Participant> findByGroup_IdAndMember(Long groupId, Member member);
    Optional<Participant> findByMemberAndGroup(Member searchMember, Group group);
    boolean existsByMemberAndGroup(Member other, Group group);
}
