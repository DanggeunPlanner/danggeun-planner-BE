package com.finalteam4.danggeunplanner.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.finalteam4.danggeunplanner.security.jwt.JwtUtil.AUTHORIZATION_ACCESS;
import static com.finalteam4.danggeunplanner.security.jwt.JwtUtil.AUTHORIZATION_REFRESH;

@Slf4j
@RequiredArgsConstructor

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String uri = request.getRequestURI();

        if(uri.contains("api/auth/signup") || uri.contains("api/auth/login") || uri.contains("api/auth/token") || uri.contains("api/auth/kakao") || uri.contains("login/kakao")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.resolveToken(request, AUTHORIZATION_ACCESS);
        String refreshToken = jwtUtil.resolveToken(request, AUTHORIZATION_REFRESH);

        //인증 필요 없거나 refrehToken이 있을 경우(토큰 재발행 요청) 다음 필터로 이동
        if (token == null || refreshToken!=null){
            filterChain.doFilter(request, response);
            return;
        }

        jwtUtil.validateAccessToken(request, response);
        Claims info = jwtUtil.getUserInfoFromToken(token,false);
        setAuthentication(info.getSubject());
        filterChain.doFilter(request,response);
    }

    // 인증/인가 설정
    private void setAuthentication(String email) {
        // 1. Security Context 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 2. Authentication 생성
        Authentication authentication = jwtUtil.createAuthentication(email);
        // 3. Context에 Authentication 넣기
        context.setAuthentication(authentication);
        // 4. Security Context Holder에 Context 넣기
        SecurityContextHolder.setContext(context);
    }

}
