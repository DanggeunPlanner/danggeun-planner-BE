package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.dto.request.MemberAuthRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberUpdateUsernameRequest;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoListResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLoginResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberMyPageResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberUpdateUsernameResponse;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.security.jwt.JwtUtil;
import com.finalteam4.danggeunplanner.member.dto.response.MemberProfileImageResponse;
import com.finalteam4.danggeunplanner.storage.service.S3UploaderService;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_MATCH_REFRESHTOKEN;
import static com.finalteam4.danggeunplanner.security.jwt.JwtUtil.AUTHORIZATION_ACCESS;
import static com.finalteam4.danggeunplanner.security.jwt.JwtUtil.AUTHORIZATION_REFRESH;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TimerRepository timerRepository;
    private final MemberValidator memberValidator;

    private final S3UploaderService s3Uploader;

    @Transactional
    public void signUp(MemberAuthRequest request) {
        memberValidator.validateEmail(request.getEmail());
        String password = passwordEncoder.encode(request.getPassword());
        Member member = request.toEntity(password);
        memberRepository.save(member);
    }

    @Transactional
    public MemberLoginResponse login(MemberAuthRequest request, HttpServletResponse response) {

        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                        () -> new DanggeunPlannerException(NOT_FOUND_MEMBER));
        memberValidator.validateMatchPassword(request.getPassword(), member.getPassword());

        boolean isExistUsername = true;
        if(member.getUsername()==null){
            isExistUsername = false;
        }

        issueTokens(response, member);
        return new MemberLoginResponse(isExistUsername);
    }

    @Transactional
    public void issueTokens(HttpServletResponse response, Member member){
        String accessToken = jwtUtil.createAccessToken(member.getEmail());
        String refreshToken = jwtUtil.createRefreshToken();
        response.addHeader(AUTHORIZATION_ACCESS, accessToken);
        response.addHeader(AUTHORIZATION_REFRESH, refreshToken);
        member.updateRefreshToken(refreshToken);
    }

    @Transactional // 이 로직은 추후 refreshToken 수정 중 변경될 수 있어서 아직 리팩토링 하지 않음
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        jwtUtil.validateRefreshToken(request);

        String accessToken = jwtUtil.resolveToken(request, AUTHORIZATION_ACCESS);
        Claims info = jwtUtil.getUserInfoFromToken(accessToken);
        Member member = memberRepository.findByEmail(info.getSubject()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        String refreshToken = jwtUtil.resolveToken(request, AUTHORIZATION_REFRESH);

        if(!member.getRefreshToken().substring(7).equals(refreshToken)){
            throw new DanggeunPlannerException(NOT_MATCH_REFRESHTOKEN);
        }

        issueTokens(response, member);
    }

    @Transactional
    public MemberUpdateUsernameResponse updateUsername(Member member, MemberUpdateUsernameRequest request) {
        String username = request.getUsername();
        memberValidator.validateUsername(username);
        member.updateUsername(username);
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

    public MemberMyPageResponse findMyPage(Member member) {
        List<Timer> timers = timerRepository.findAllByMemberAndIsFinish(member,true);
        Integer totalCarrot = timers.size();
        return new MemberMyPageResponse(member, totalCarrot);
    }

    @Transactional
    public MemberProfileImageResponse uploadImage(Member member, MultipartFile image)throws IOException {
        String storedFileName = s3Uploader.upload(image, "images");

        Member memberForImageUpload = memberRepository.findByUsername(member.getUsername()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        memberForImageUpload.updateProfileImage(storedFileName);

        return new MemberProfileImageResponse(memberForImageUpload);
    }

}