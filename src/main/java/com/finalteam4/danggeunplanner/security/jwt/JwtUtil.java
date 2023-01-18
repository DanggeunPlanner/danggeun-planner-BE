package com.finalteam4.danggeunplanner.security.jwt;

import com.finalteam4.danggeunplanner.common.exception.ErrorCode;
import com.finalteam4.danggeunplanner.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    public static final String AUTHORIZATION_REFRESH = "RefreshToken";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 24*60*60*1000L;
    @Value("${jwt.secret.key.access}")
    private String accessTokenSecretKey;
    @Value("${jwt.secret.key.refresh}")
    private String refreshTokenSecretKey;
    private Key accessTokenKey;
    private Key refreshTokenKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;  //사용할 암호화 알고리즘
    private final UserDetailsServiceImpl userDetailsService;

    @PostConstruct
    public void init(){
        byte[] accessTokenBytes = Base64.getDecoder().decode(accessTokenSecretKey); //Base64로 인코딩 되어 있는 것을 secretKey로 decode
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenBytes);

        byte[] refreshTokenBytes = Base64.getDecoder().decode(refreshTokenSecretKey);
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenBytes);
    }
    public String resolveToken(HttpServletRequest request, String authorization) {
        String bearerToken = request.getHeader(authorization);
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
                        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(accessTokenKey, signatureAlgorithm)
                        .compact();
    }
    public String createRefreshToken(){
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME +  2 * 24 * 60 * 60 * 1000L))
                        .setIssuedAt(date)
                        .signWith(refreshTokenKey, signatureAlgorithm)
                        .compact();
    }
    public boolean validateAccessToken(HttpServletRequest request,  HttpServletResponse response) {
        try {
            Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(request.getHeader(AUTHORIZATION_ACCESS).substring(7));
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.INVALID_ACCESSTOKEN.getCode());
        } catch (ExpiredJwtException e) {
                e.printStackTrace();
                request.setAttribute("exception", ErrorCode.EXPIRATION_ACCESSTOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.ACCESSTOKEN_NOT_SUPPORT.getCode());
        }
        catch (JwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.UNKNOWN_ACCESSTOKEN_ERROR.getCode());
        }
        return false;
    }
    public String validateRefreshToken(HttpServletRequest request, String email){
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(refreshTokenKey).build().parseClaimsJws(request.getHeader(AUTHORIZATION_REFRESH).substring(7));

            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
            if (!claims.getBody().getExpiration().before(new Date())) {
                return recreationAccessToken(email);
            }
        } catch (SecurityException | MalformedJwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.INVALID_REFRESHTOKEN.getCode());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.EXPIRATION_REFRESHTOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.REFRESHTOKEN_NOT_SUPPORT.getCode());
        }
        catch (JwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.UNKNOWN_REFRESHTOKEN_ERROR.getCode());
        }
        return null;
    }

    public String recreationAccessToken(String userEmail){

        Claims claims = Jwts.claims().setSubject(userEmail); // JWT payload 에 저장되는 정보단위
        Date now = new Date();

        //Access Token
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TOKEN_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, accessTokenSecretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    public Claims getUserInfoFromToken(String token) {
            return Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token).getBody();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, null);
    }
}




