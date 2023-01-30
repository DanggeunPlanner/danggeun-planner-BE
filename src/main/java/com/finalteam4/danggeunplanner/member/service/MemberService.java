package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.dto.request.MemberAuthRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberDisclosureRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberUpdateUsernameRequest;
import com.finalteam4.danggeunplanner.member.dto.request.OauthLoginRequest;
import com.finalteam4.danggeunplanner.member.dto.response.MemberDisclosureResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoListResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLoginResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberMyPageResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberProfileImageResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberRanking;
import com.finalteam4.danggeunplanner.member.dto.response.MemberRankingsResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberUpdateUsernameResponse;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.redis.service.RedisService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import com.finalteam4.danggeunplanner.security.jwt.JwtUtil;
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
import java.time.Duration;
import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_MATCH_REFRESHTOKEN;
import static com.finalteam4.danggeunplanner.security.jwt.JwtUtil.AUTHORIZATION_ACCESS;
import static com.finalteam4.danggeunplanner.security.jwt.JwtUtil.AUTHORIZATION_REFRESH;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TimerRepository timerRepository;
    private final MemberValidator memberValidator;
    private final S3UploaderService s3Uploader;
    private final GroupRepository groupRepository;
    private final RedisService redisService;

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
        issueTokens(response, member.getEmail());
        boolean isExistUsername = memberValidator.validateExistUsername(member);
        return new MemberLoginResponse(isExistUsername);
    }

    @Transactional
    public void issueTokens(HttpServletResponse response, String email){
        String accessToken = jwtUtil.createAccessToken(email);
        String refreshToken = jwtUtil.createRefreshToken();
        response.addHeader(AUTHORIZATION_ACCESS, accessToken);
        response.addHeader(AUTHORIZATION_REFRESH, refreshToken);
        redisService.setValues(email, refreshToken, Duration.ofDays(2));
    }

    @Transactional
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenFromRequest = request.getHeader(AUTHORIZATION_REFRESH); //요청헤더에서 온 RTK
        String token = jwtUtil.resolveToken(request, AUTHORIZATION_ACCESS); //요청헤더에서 온 ATK(bearer 제외)
        Claims info = jwtUtil.getUserInfoFromToken(token, true); //ATK에서 body가지고 옴
        String email = info.getSubject(); //가지고온 body에서 subject 빼오기 = email
        String refreshTokenFromRedis = redisService.getValues(email);
        if(refreshTokenFromRequest.equals(refreshTokenFromRedis)){
            jwtUtil.validateRefreshToken(request, email);
            issueTokens(response, email);
        } else {
            throw new DanggeunPlannerException(NOT_MATCH_REFRESHTOKEN);
        }
    }

    @Transactional
    public void logout(Member member){
        redisService.delValues(member.getEmail());
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
        MemberInfoListResponse memberInfoListResponse = new MemberInfoListResponse();
        for(Member other : members){
            if(member.getUsername().equals(other.getUsername()))
                continue;
            memberInfoListResponse.add(new MemberInfoResponse(other));
        }
        return memberInfoListResponse;
    }

    public MemberMyPageResponse findMyPage(Member member) {
        List<Timer> timers = timerRepository.findAllByMemberAndIsFinish(member,true);

        Integer totalCarrot = 0;

        for(Timer timer : timers){
            totalCarrot += timer.getCount();
        }
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

        issueTokens(response, member.getEmail());
        boolean isExistUsername = memberValidator.validateExistUsername(member);
        return new MemberLoginResponse(isExistUsername);
    }
    @Transactional
    public MemberDisclosureResponse setPlannerPublic(UserDetailsImpl userDetails, MemberDisclosureRequest request) {
        Member member = memberRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        member.updatePlannerOpened(request.getIsPlannerOpened());

        return new MemberDisclosureResponse(member.getIsPlannerOpened());
    }
}