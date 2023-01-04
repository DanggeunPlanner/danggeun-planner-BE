package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.dto.request.MemberSignUpRequest;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DUPLICATED_EMAIL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(MemberSignUpRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        memberRepository.findByEmail(email)
                .ifPresent( m -> {
                    throw new DanggeunPlannerException(DUPLICATED_EMAIL);
                });

        Member member = new Member(email, password);
        memberRepository.save(member);
    }
}