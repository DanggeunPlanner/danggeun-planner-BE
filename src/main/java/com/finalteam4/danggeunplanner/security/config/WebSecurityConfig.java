package com.finalteam4.danggeunplanner.security.config;

import com.finalteam4.danggeunplanner.security.exception.CustomAuthenticationEntryPoint;
import com.finalteam4.danggeunplanner.security.jwt.JwtAuthFilter;
import com.finalteam4.danggeunplanner.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().
                antMatchers("/api/auth/**").permitAll().
                antMatchers("/login/kakao").permitAll().
                anyRequest().authenticated().

                and().
                addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class).
                exceptionHandling().
                authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://www.dggnplanner.com/");
        configuration.addAllowedOrigin("http://danggeunplanner.s3-website.ap-northeast-2.amazonaws.com/");
        configuration.addAllowedOrigin("http://localhost:8080/login/kakao");
        configuration.addAllowedOrigin("https://danggeun-planner-fe.vercel.app");
        configuration.addAllowedOrigin("https://danggeun-planner-9rfbtpqwd-dahyejang.vercel.app");
        configuration.addAllowedOrigin("https://danggeun-planner-fe-new.vercel.app");
        configuration.addAllowedMethod("*"); // 허용할 Http Method
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // 내 서버가 응답할 때 json을 js에서 처리할 수 있게 설정
        configuration.setMaxAge(3600L);
        configuration.addExposedHeader("AccessToken"); // 헤더에 있는 JWT 토큰을 클라이언트에서 사용할 수 있도록 권한을 주는 부분
        configuration.addExposedHeader("RefreshToken");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    }


