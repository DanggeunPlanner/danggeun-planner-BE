package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.dto.request.MemberCreateUsernameRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberLogInRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberSignUpRequest;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLogInResponse;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import com.finalteam4.danggeunplanner.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DUPLICATED_NICKNAME;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_PASSWORD;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(MemberSignUpRequest request) {
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());

        memberRepository.findByEmail(email)
                .ifPresent( m -> {
                    throw new DanggeunPlannerException(DUPLICATED_EMAIL);
                });

        Member member = new Member(email, password);
        member.updateUsername("");
        memberRepository.save(member);
    }

    @Transactional
    public MemberLogInResponse logIn(MemberLogInRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        String password = request.getPassword();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                        () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
                );

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new DanggeunPlannerException(NOT_VALID_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_ACCESS, jwtUtil.createAccessToken(member.getEmail()));

        Boolean isExistUsername = true;

        if(member.getUsername().equals("")){
            isExistUsername = false;
        }

        return new MemberLogInResponse(isExistUsername);
    }

    @Transactional
    public void updateUsername(UserDetailsImpl userDetails, MemberCreateUsernameRequest request) {

        if(memberRepository.existsByUsername(request.getUsername())){
           throw new DanggeunPlannerException(DUPLICATED_NICKNAME);
        }

        String eamil = userDetails.getMember().getEmail();

        Member member = memberRepository.findByEmail(eamil).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );


        member.updateUsername(request.getUsername());
    }

    public MemberInfoResponse findMember(String username) {

        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        return new MemberInfoResponse(findMember.getId(), findMember.getUsername(), findMember.getProfileImage());

    }
}