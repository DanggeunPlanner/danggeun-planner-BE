package com.finalteam4.danggeunplanner.security.jwt;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.TOKEN_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;
    @Value("${jwt.secret.key.access}")
    private String accessTokenSecretKey;
    private Key accessTokenKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;  //사용할 암호화 알고리즘
    private final UserDetailsServiceImpl userDetailsService;

    @PostConstruct
    public void init(){
        byte[] accessTokenBytes = Base64.getDecoder().decode(accessTokenSecretKey); //Base64로 인코딩 되어 있는 것을 secretKey로 decode
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenBytes);
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_ACCESS);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String createAccessToken(String email) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(accessTokenKey, signatureAlgorithm)
                        .compact();
    }

    // Access 토큰 검증
    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(accessToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid Access JWT signature, 유효하지 않는 Access JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            // 만료된 경우 토큰 재발급
            log.info("Expired Access JWT, 만료된 Access JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported Access JWT, 지원되지 않는 Access JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("Access JWT claims is empty, 잘못된 Access JWT 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
            return Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token).getBody();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email); // email을 통해 사용자 조회
        return new UsernamePasswordAuthenticationToken(userDetails, null, null); //userDetail 및 권한 넣어 생성
    }



}




