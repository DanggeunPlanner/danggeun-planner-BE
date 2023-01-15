package com.finalteam4.danggeunplanner.security.exception;

import com.finalteam4.danggeunplanner.common.exception.ErrorCode;
import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, java.io.IOException {
        String exception = (String)request.getAttribute("exception");
        ErrorCode errorCode;

        if(exception == null) {
            errorCode = ErrorCode.NOT_AUTHORIZED_MEMBER;
        }
        //Access : 토큰 만료된 경우
        else if(exception.equals(ErrorCode.EXPIRATION_ACCESSTOKEN.getCode())) {
            errorCode = ErrorCode.EXPIRATION_ACCESSTOKEN;
        }
        //Access : 유효하지 않은 토큰인 경우
        else if(exception.equals(ErrorCode.INVALID_ACCESSTOKEN.getCode())) {
            errorCode = ErrorCode.INVALID_ACCESSTOKEN;
        }
        else if(exception.equals(ErrorCode.ACCESSTOKEN_NOT_SUPPORT.getCode())) {
            errorCode = ErrorCode.ACCESSTOKEN_NOT_SUPPORT;
        }
        else if(exception.equals(ErrorCode.UNKNOWN_ACCESSTOKEN_ERROR.getCode())) {
            errorCode = ErrorCode.UNKNOWN_ACCESSTOKEN_ERROR;
        }
        //Refresh : 토큰 만료된 경우
        else if(exception.equals(ErrorCode.EXPIRATION_REFRESHTOKEN.getCode())) {
            errorCode = ErrorCode.EXPIRATION_REFRESHTOKEN;
        }
        //Refresh : 유효하지 않은 토큰인 경우
        else if(exception.equals(ErrorCode.INVALID_REFRESHTOKEN.getCode())) {
            errorCode = ErrorCode.INVALID_REFRESHTOKEN;
        }
        else if(exception.equals(ErrorCode.REFRESHTOKEN_NOT_SUPPORT.getCode())) {
            errorCode = ErrorCode.REFRESHTOKEN_NOT_SUPPORT;
        }
        //Refresh : 그외
        else if(exception.equals(ErrorCode.UNKNOWN_REFRESHTOKEN_ERROR.getCode())) {
            errorCode = ErrorCode.UNKNOWN_REFRESHTOKEN_ERROR;
        }
        else {
            errorCode = ErrorCode.NOT_AUTHORIZED_MEMBER;
        }
        setResponse(response, errorCode);


    }
    //한글 출력을 위해 getWriter()
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException, java.io.IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
                + "\", \"code\" : \"" +  errorCode.getCode()
                + "\", \"status\" : " + errorCode.getHttpStatus()
                + ", \"errors\" : [ ] }");
    }
}

