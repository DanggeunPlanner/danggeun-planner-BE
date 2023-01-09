package com.finalteam4.danggeunplanner.storage.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.storage.dto.ProfileImageResponse;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileImageService {

    private final MemberRepository memberRepository;
    private final S3UploaderService s3Uploader;
    @Transactional
    public ProfileImageResponse uploadImage(Member member, MultipartFile image)throws IOException {
        String storedFileName = s3Uploader.upload(image, "images");

        Member memberForImageUpload = memberRepository.findByUsername(member.getUsername()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        memberForImageUpload.updateProfileImage(storedFileName);

        return new ProfileImageResponse(memberForImageUpload);
    }
}
