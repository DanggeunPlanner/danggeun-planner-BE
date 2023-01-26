package com.finalteam4.danggeunplanner.member.controller;

import com.finalteam4.danggeunplanner.common.exception.ValidationSequence;
import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.member.dto.request.MemberAuthRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberDisclosureRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberUpdateUsernameRequest;
import com.finalteam4.danggeunplanner.member.dto.request.OauthLoginRequest;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoListResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLoginResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLogoutResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberMyPageResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberProfileImageResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberUpdateUsernameResponse;
import com.finalteam4.danggeunplanner.member.service.MemberService;
import com.finalteam4.danggeunplanner.member.service.OauthService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final OauthService oauthService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseMessage<Void>> signUp(@Validated(ValidationSequence.class) @RequestBody MemberAuthRequest request){
        memberService.signUp(request);
        return new ResponseEntity<>(new ResponseMessage<>("회원가입 성공",null), HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseMessage<MemberLoginResponse>> login(@Validated(ValidationSequence.class) @RequestBody MemberAuthRequest request, HttpServletResponse response){
        MemberLoginResponse memberLogInResponse = memberService.login(request, response);
        return new ResponseEntity<>(new ResponseMessage<>("로그인 성공", memberLogInResponse), HttpStatus.OK);
    }

    @PostMapping("/auth/token")
    public ResponseEntity<ResponseMessage<Void>> reissueToken(HttpServletRequest request, HttpServletResponse response){
        memberService.reissueToken(request, response);
        return new ResponseEntity<>(new ResponseMessage<>("토큰 재발행 성공", null), HttpStatus.ACCEPTED);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ResponseMessage<MemberLogoutResponse>> logout(@AuthenticationPrincipal UserDetailsImpl userDetails,HttpServletRequest request){
        memberService.logout(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("로그아웃 성공", null), HttpStatus.OK);
    }


    @PutMapping("/member/username")
    public ResponseEntity<ResponseMessage<MemberUpdateUsernameResponse>> updateUsername(@AuthenticationPrincipal UserDetailsImpl userDetails, @Validated(ValidationSequence.class) @RequestBody MemberUpdateUsernameRequest request){
        MemberUpdateUsernameResponse response = memberService.updateUsername(userDetails, request);
        return new ResponseEntity<>(new ResponseMessage<>("닉네임 변경 성공", response), HttpStatus.ACCEPTED);
    }

    @GetMapping("/member/search/{username}")
    public ResponseEntity<ResponseMessage<MemberInfoListResponse>> find(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String username){
        MemberInfoListResponse response = memberService.find(userDetails.getMember(), username);
        return new ResponseEntity<>(new ResponseMessage<>("회원 검색 성공", response), HttpStatus.OK);
    }

    @GetMapping("/member/mypage")
    public ResponseEntity<ResponseMessage<MemberMyPageResponse>> findMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        MemberMyPageResponse response = memberService.findMyPage(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("마이페이지 조회 성공", response), HttpStatus.OK);
    }

    @PutMapping("/member/image")
    public ResponseEntity<ResponseMessage<MemberProfileImageResponse>> uploadMemberImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestParam(value="image") MultipartFile image)throws IOException {
        MemberProfileImageResponse response = memberService.uploadImage(userDetails.getMember(), image);
        return new ResponseEntity<>(new ResponseMessage<>("프로필 사진 변경 성공", response), HttpStatus.ACCEPTED);
    }

    @GetMapping("/auth/kakao")
    public ResponseEntity<ResponseMessage<MemberLoginResponse>> kakaoLogin(@RequestParam String code, HttpServletResponse response){
        String kakaoAccessToken = oauthService.getKakaoAccessToken(code); //받은 코드로 액세스토큰 받기
        OauthLoginRequest kakaoLoginRequest = oauthService.createKakaoUser(kakaoAccessToken); //받아온 액세스토큰으로 카카오 로그인 리퀘스트 만들기
        MemberLoginResponse memberLogInResponse = memberService.OauthLogin(kakaoLoginRequest, response);
        return new ResponseEntity<>(new ResponseMessage<>("카카오 로그인 성공", memberLogInResponse), HttpStatus.ACCEPTED);
    }

    @PutMapping("/member/disclosure")
    public ResponseEntity<ResponseMessage<Void>> setPlannerPublic(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MemberDisclosureRequest request){
        memberService.setPlannerPublic(userDetails, request);
        return new ResponseEntity<>(new ResponseMessage<>("공개범위 설정 변경 완료", null), HttpStatus.ACCEPTED);
    }
}

