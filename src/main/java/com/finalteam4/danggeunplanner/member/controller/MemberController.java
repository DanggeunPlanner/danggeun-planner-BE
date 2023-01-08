package com.finalteam4.danggeunplanner.member.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.member.dto.request.MemberCreateUsernameRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberLogInRequest;
import com.finalteam4.danggeunplanner.member.dto.request.MemberSignUpRequest;
import com.finalteam4.danggeunplanner.member.dto.response.MemberInfoResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberLogInResponse;
import com.finalteam4.danggeunplanner.member.dto.response.MemberMyPageResponse;

import com.finalteam4.danggeunplanner.member.service.MemberService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberSignUpRequest request){

        memberService.signUp(request);

        return new ResponseEntity<>(new ResponseMessage("회원가입 성공",null), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody MemberLogInRequest request, HttpServletResponse response){

        MemberLogInResponse memberLogInResponse = memberService.logIn(request, response);

        return new ResponseEntity<>(new ResponseMessage("로그인 성공", memberLogInResponse), HttpStatus.OK);
    }

    @PostMapping("/username")
    public ResponseEntity<?> createUsername(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MemberCreateUsernameRequest request){
        memberService.updateUsername(userDetails, request);
        return new ResponseEntity<>(new ResponseMessage("닉네임 등록 성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> find(@PathVariable String username){

        MemberInfoResponse response = memberService.findMember(username);
        return new ResponseEntity<>(new ResponseMessage("회원 검색 성공", response), HttpStatus.OK);

    }

    @GetMapping("/mypage")
    public ResponseEntity<?> findMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){

        MemberMyPageResponse response = memberService.findMyPage(userDetails);
        return new ResponseEntity<>(new ResponseMessage("마이페이지 조회 성공", response), HttpStatus.OK);

    }


}

