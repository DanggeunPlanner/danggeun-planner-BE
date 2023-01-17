package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.dto.request.MemberAuthRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberUpdateUsernameRequest;
import com.finalteam4.danggeunplanner.member.dto.request.OauthLoginRequest;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoListResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLoginResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberMyPageResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberProfileImageResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberUpdateUsernameResponse;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import com.finalteam4.danggeunplanner.security.jwt.JwtUtil;
import com.finalteam4.danggeunplanner.storage.service.S3UploaderService;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
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
    private final GroupRepository groupRepository;

    private final OauthLoginRequest oauthLoginRequest;


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
        issueTokens(response, member);
        boolean isExistUsername = memberValidator.validateExistUsername(member);
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

    @Transactional //리팩토링 진행 전(로직 수정 가능)
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenWithBearer = request.getHeader(AUTHORIZATION_REFRESH);
        Member member = memberRepository.findByRefreshToken(refreshTokenWithBearer);
        jwtUtil.validateRefreshToken(request, member.getEmail());

        jwtUtil.resolveToken(request, AUTHORIZATION_REFRESH);
        issueTokens(response, member);
    }

    @Transactional
    public MemberUpdateUsernameResponse updateUsername(UserDetailsImpl userDetails, MemberUpdateUsernameRequest request) {
        String username = request.getUsername();
        memberValidator.validateUsername(username);
       
        if (groupRepository.existsByAdmin(userDetails.getUsername())){
            Group group = groupRepository.findByAdmin(userDetails.getUsername()).orElseThrow(
                    () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
            );
            group.updateAdmin(request.getUsername());
        }

        Member member = memberRepository.findById(userDetails.getMember().getId()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        member.updateUsername(username);
        return new MemberUpdateUsernameResponse(member);
    }

    public MemberInfoListResponse find(Member member, String username) {

        List<Member> members = memberRepository.findByUsernameStartsWithOrderByUsername(username);
        members.remove(member);

        MemberInfoListResponse memberInfoListResponse = new MemberInfoListResponse();
        for(Member other : members){
            memberInfoListResponse.add(new MemberInfoResponse(other));
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

    @Transactional
    public MemberLoginResponse OauthLogin(OauthLoginRequest oauthLoginRequest, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(oauthLoginRequest.getEmail()).orElse(null);

        //카카오계정으로 가입된 회원이 없으면 회원가입 진행하고 토큰 발행, 있으면 그냥 토큰 발행
        if(member == null){
            member = oauthLoginRequest.toEntity(oauthLoginRequest.getEmail());
            memberRepository.save(member);
        }

        issueTokens(response, member);
        boolean isExistUsername = memberValidator.validateExistUsername(member);
        return new MemberLoginResponse(isExistUsername);
    }
}