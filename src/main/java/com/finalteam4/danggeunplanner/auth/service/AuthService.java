package com.finalteam4.danggeunplanner.auth.service;

import com.finalteam4.danggeunplanner.auth.dto.SignUpRequestDto;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DUPLICATED_EMAIL;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {


    private final MemberRepository memberRepository;
    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.getEmail();
        String password = signUpRequestDto.getPassword();

        memberRepository.findByEmail(email)
                .ifPresent( m -> {
                    throw new DanggeunPlannerException(DUPLICATED_EMAIL);
                });

        memberRepository.save(Member.signUpMember(email, password));

    }
}
