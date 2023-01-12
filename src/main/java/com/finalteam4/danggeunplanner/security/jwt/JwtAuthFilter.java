package com.finalteam4.danggeunplanner.security.jwt;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
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

@Slf4j
@RequiredArgsConstructor

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String uri = request.getRequestURI();

        if(uri.contains("api/member/signup") || uri.contains("api/member/login")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.resolveToken(request, AUTHORIZATION_ACCESS);

        //인증 필요 없는 부분은 그냥 체인 통과하도록 분기처리(필요없으면 그냥 다음 필터로 이동)
        if (token == null){
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("===============check");
        jwtUtil.validateAccessToken(request, response);

        //토큰에서 "sub" 부분 추출해서 인증객체생성
        Claims info = jwtUtil.getUserInfoFromToken(token);
        setAuthentication(info.getSubject());

        // 4. 다음 필터로 보냄
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
