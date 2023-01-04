package com.finalteam4.danggeunplanner.auth.controller;

import com.finalteam4.danggeunplanner.auth.dto.SignUpRequestDto;
import com.finalteam4.danggeunplanner.auth.service.AuthService;
import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto){

       authService.signUp(signUpRequestDto);

       return new ResponseEntity<>(new ResponseMessage("회원가입 성공",null), HttpStatus.CREATED);
    }

}

