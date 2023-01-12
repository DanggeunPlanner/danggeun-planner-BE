package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberValidator {

    public void validateAccess(Member member , Member other){
        if(!member.getId().equals(other.getId())){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
}
