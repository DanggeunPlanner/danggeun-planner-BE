package com.finalteam4.danggeunplanner.storage.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.storage.dto.ProfileImageResponse;
import com.finalteam4.danggeunplanner.storage.service.ProfileImageService;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/image")
public class ProfileImageController {
    private final ProfileImageService profileImageService;

    @PutMapping
    public ResponseEntity<?> uploadMemberImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestParam(value="image") MultipartFile image)throws IOException{
        Member member = userDetails.getMember();
        ProfileImageResponse response = profileImageService.uploadImage(member, image);

        return new ResponseEntity<>(new ResponseMessage("프로필 사진 변경 성공", response), HttpStatus.ACCEPTED);


    }

}

