package com.finalteam4.danggeunplanner.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //global
    NOT_VALID_FORMAT(HttpStatus.BAD_REQUEST, "NOT_VALID_FORMAT", "지정된 양식을 사용해주세요."),
    NOT_VALID_ACCESS(HttpStatus.BAD_REQUEST,"NOT_VALID_ACCESS","접근 권한이 없습니다."),
    
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_001", "이미 사용 중인 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER_002", "이미 사용 중인 닉네임입니다."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_003", "비밀번호를 다시 확인해주세요."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER_004", "찾을 수 없는 회원입니다."),
    NOT_MATCH_REFRESHTOKEN(HttpStatus.NOT_ACCEPTABLE, "MEMBER_005", "Refresh Token이 일치하지 않습니다."),
    
    ACCESSTOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT_001", "Access Token이 존재하지 않습니다."),
    REFRESHTOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT_002", "Refresh Token이 존재하지 않습니다."),
    NOT_VALID_ACCESSTOKEN(HttpStatus.BAD_REQUEST, "JWT_003", "Access Token이 유효하지 않습니다."),
    NOT_VALID_REFRESHTOKEN(HttpStatus.BAD_REQUEST, "JWT_004", "Refresh Token이 유효하지 않습니다."),
    EXPIRATION_ACCESSTOKEN(HttpStatus.BAD_REQUEST, "JWT_005", "Access Token이 만료되었습니다"),
    EXPIRATION_REFRESHTOKEN(HttpStatus.BAD_REQUEST, "JWT_006", "Refresh Token이 만료되었습니다"),
    NOT_AUTHORIZED_MEMBER(HttpStatus.BAD_REQUEST, "JWT_007", "인가되지 않은 사용자입니다."),

    NOT_FOUND_TIMER(HttpStatus.NOT_FOUND,"TIMER_001","찾을 수 없는 타이머입니다."),
    IS_RUNNING_TIMER(HttpStatus.BAD_REQUEST,"TIMER_002","실행중인 타이머입니다."),
    EXPIRED_TIMER(HttpStatus.BAD_REQUEST, "TIMER_003", "만료된 타이머입니다."),
    ALREADY_FINISHED_TIMER(HttpStatus.BAD_REQUEST, "TIMER_004", "이미 완료된 타이머입니다."),

    NOT_FOUND_PLAN(HttpStatus.NOT_FOUND,"PLAN_001","찾을 수 없는 계획입니다."),

    NOT_FOUND_GROUP(HttpStatus.NOT_FOUND,"GROUP_001","찾을 수 없는 그룹입니다."),
    NOT_FOUND_JOIN_GROUP(HttpStatus.NOT_FOUND,"GROUP_002","가입한 그룹이 없습니다."),

    NOT_FOUND_PARTICIPANT(HttpStatus.NOT_FOUND,"PARTICIPANT_001","찾을 수 없는 참가자입니다."),
    NOT_DELETE_PARTICIPANT(HttpStatus.BAD_REQUEST,"PARTICIPANT_002","관리자는 그룹을 탈퇴할 수 없습니다."),

    NOT_FOUND_PLANNER(HttpStatus.NOT_FOUND,"PLANNER_001","찾을 수 없는 플래너입니다."),

    NOT_FOUND_CALENDAR(HttpStatus.NOT_FOUND,"CALENDAR_001","찾을 수 없는 캘린더입니다."),

    NOT_FOUND_INVITATION(HttpStatus.NOT_FOUND,"INVITATION_001","찾을 수 없는 초대 리스트입니다."),
    EXCEED_INVITATION_MAX_SIZE(HttpStatus.BAD_REQUEST, "INVITATION_002", "초대가능한 인원은 최대 30명입니다."),
    ALREADY_INVITED_MEMBER(HttpStatus.BAD_REQUEST, "INVITATION_003", "이미 초대리스트에 있는 회원입니다."),
    ALREADY_PARTICIPATED_MEMBER(HttpStatus.BAD_REQUEST, "INVITATION_004", "이미 그룹에 있는 참가자입니다."),
    NOT_INVITED_MEMBER(HttpStatus.NOT_FOUND,"INVITATION_005","초대리스트에 존재하지 않는 회원입니다.")
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
