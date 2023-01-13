package com.finalteam4.danggeunplanner.member.repository;


import com.finalteam4.danggeunplanner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);
    List<Member> findByUsernameStartsWithOrderByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsInvitationByUsername(Member member);
}
