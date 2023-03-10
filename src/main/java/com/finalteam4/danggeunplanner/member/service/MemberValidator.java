package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DUPLICATED_NICKNAME;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_PASSWORD;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberValidator {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public void validateAccess(Member member , Member other){
        if(!member.getId().equals(other.getId())){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
    public void validateEmail(String email){
        memberRepository.findByEmail(email)
                .ifPresent( m -> {
                    throw new DanggeunPlannerException(DUPLICATED_EMAIL);
        });
    }
    public void validateMatchPassword(String storedPassword, String password){
        if(!passwordEncoder.matches(storedPassword, password)){
            throw new DanggeunPlannerException(NOT_VALID_PASSWORD);
        }
    }
    public void validateUsername(String username){
        if(memberRepository.existsByUsername(username)){
            throw new DanggeunPlannerException(DUPLICATED_NICKNAME);
        }
    }

    public boolean validateExistUsername(Member member){
        boolean isExistUsername = true;
        if(member.getUsername()==null){
            isExistUsername = false;
        }
        return isExistUsername;
    }
}
