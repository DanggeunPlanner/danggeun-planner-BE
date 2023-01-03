package com.finalteam4.danggeunplanner.member.repository;


import com.finalteam4.danggeunplanner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
