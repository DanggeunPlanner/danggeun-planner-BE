package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.dto.request.MemberLoginRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberSignUpRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberUpdateUsernameRequest;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoListResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLogInResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberMyPageResponse;

import com.finalteam4.danggeunplanner.member.dto.response.MemberUpdateUsernameResponse;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import com.finalteam4.danggeunplanner.security.jwt.JwtUtil;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

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
    private final TimerRepository timerRepository;

    @Transactional
    public void signUp(MemberSignUpRequest request) {
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());

        memberRepository.findByEmail(email)
                .ifPresent( m -> {
                    throw new DanggeunPlannerException(DUPLICATED_EMAIL);
                });

        Member member = request.toEntity(password);
        memberRepository.save(member);
    }

    @Transactional
    public MemberLogInResponse login(MemberLoginRequest request, HttpServletResponse response) {
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
        if(member.getUsername()==null){
            isExistUsername = false;
        }

        return new MemberLogInResponse(isExistUsername);
    }

    @Transactional
    public MemberUpdateUsernameResponse updateUsername(UserDetailsImpl userDetails, MemberUpdateUsernameRequest request) {

        if(memberRepository.existsByUsername(request.getUsername())){
           throw new DanggeunPlannerException(DUPLICATED_NICKNAME);
        }

        String email = userDetails.getMember().getEmail();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        member.updateUsername(request.getUsername());
        return new MemberUpdateUsernameResponse(member);
    }

    public MemberInfoListResponse find(String username) {

        List<Member> members = memberRepository.findByUsernameStartsWithOrderByUsername(username);

        MemberInfoListResponse memberInfoListResponse = new MemberInfoListResponse();
        for(Member member : members){
            memberInfoListResponse.add(new MemberInfoResponse(member));
        }
        return memberInfoListResponse;
    }


    public MemberMyPageResponse findMyPage(UserDetailsImpl userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        List<Timer> timers = timerRepository.findAllByMember(userDetails.getMember());
        Integer totalCarrot = timers.size();

        return new MemberMyPageResponse(member, totalCarrot);
    }
}