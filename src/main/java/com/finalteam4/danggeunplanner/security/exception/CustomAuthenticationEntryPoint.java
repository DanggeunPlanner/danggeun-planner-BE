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
            errorCode = ErrorCode.ACCESSTOKEN_NOT_FOUND;
        }
        //Access : 토큰 만료된 경우
        else if(exception.equals(ErrorCode.EXPIRATION_ACCESSTOKEN.getCode())) {
            errorCode = ErrorCode.EXPIRATION_ACCESSTOKEN;
        }
        //Access : 유효하지 않은 토큰인 경우
        else if(exception.equals(ErrorCode.NOT_VALID_ACCESSTOKEN.getCode())) {
            errorCode = ErrorCode.NOT_VALID_ACCESSTOKEN;
        }
        //Refresh : 토큰 만료된 경우
        else if(exception.equals(ErrorCode.EXPIRATION_REFRESHTOKEN.getCode())) {
            errorCode = ErrorCode.EXPIRATION_REFRESHTOKEN;
        }
        //Refresh : 유효하지 않은 토큰인 경우
        else if(exception.equals(ErrorCode.NOT_VALID_REFRESHTOKEN.getCode())) {
            errorCode = ErrorCode.NOT_VALID_REFRESHTOKEN;
        }
        //Refresh : 그외
        else if(exception.equals(ErrorCode.REFRESHTOKEN_NOT_FOUND.getCode())) {
            errorCode = ErrorCode.REFRESHTOKEN_NOT_FOUND;
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

