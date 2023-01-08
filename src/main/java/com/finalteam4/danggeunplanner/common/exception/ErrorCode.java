package com.finalteam4.danggeunplanner.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_VALID_FORMAT(HttpStatus.BAD_REQUEST, "NOT_VALID_FORMAT", "지정된 양식을 사용해주세요."),
    NOT_VALID_ACCESS(HttpStatus.BAD_REQUEST,"NOT_VALID_ACCESS","접근 권한이 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_001", "이미 사용 중인 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER_002", "이미 사용 중인 닉네임입니다."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_003", "비밀번호를 다시 확인해주세요."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER_004", "찾을 수 없는 사용자입니다."),


    //jwt
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT_001", "토큰이 존재하지 않습니다."),
    NOT_VALID_TOKEN(HttpStatus.BAD_REQUEST, "JWT_002", "토큰이 유효하지 않습니다."),
    NOT_AUTHORIZED_MEMBER(HttpStatus.BAD_REQUEST, "JWT_003", "인가되지 않은 사용자입니다."),

    EXPIRATION_TOKEN(HttpStatus.BAD_REQUEST, "JWT_004", "Access Token이 만료되었습니다"),

    NOT_FOUND_PLAN(HttpStatus.NOT_FOUND,"PLAN_001","찾을 수 없는 계획입니다."),

    NOT_FOUND_PLANNER(HttpStatus.NOT_FOUND,"PLANNER_001","찾을 수 없는 플래너입니다."),
    NOT_FOUND_CALENDAR(HttpStatus.NOT_FOUND,"CALENDAR_001","찾을 수 없는 캘린더입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
