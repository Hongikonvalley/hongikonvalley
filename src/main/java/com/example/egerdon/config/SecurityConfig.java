package com.example.egerdon.config;

import com.example.egerdon.filter.JwtAuthenticationFilter;
import com.example.egerdon.handler.OAuth2FailureHandler;
import com.example.egerdon.handler.OAuth2SuccessHandler;
import com.example.egerdon.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Security JWT 기반 설정
 * 
 * 주요 구성 요소:
 * - JWT 인증 필터 등록
 * - OAuth2 로그인 설정 (카카오)
 * - CORS 설정
 * - 경로별 접근 권한 설정
 * - 세션 정책 (Stateless)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    /**
     * Security Filter Chain 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (JWT 사용으로 불필요)
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS 설정 적용
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 세션 정책: Stateless (JWT 사용)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
            // API 경로에서 인증 실패 시 401 JSON 응답 (리다이렉트 방지)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    // API 경로인 경우 401 JSON 응답
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write(
                            "{\"statusCode\":401,\"message\":\"인증이 필요합니다.\",\"data\":null}"
                        );
                    } else {
                        // OAuth2 로그인 시작 페이지로 리다이렉트
                        response.sendRedirect("/oauth2/authorization/kakao");
                    }
                })
            )
            
            // 경로별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 공개 접근 허용 경로
                .requestMatchers(
                    "/",
                    "/login/**",
                    "/oauth2/**",
                    "/error"
                ).permitAll()
                
                // 인증 필요 API - 모든 비즈니스 API는 인증 필요
                .requestMatchers(
                    "/api/v1/auth/me",       // 현재 사용자 정보 조회
                    "/api/v1/auth/validate"  // JWT 토큰 검증
                ).authenticated()
                
                // 그 외 모든 요청은 허용 (웹 페이지용)
                .anyRequest().permitAll()
            )
            
            // OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> 
                    userInfo.userService(customOAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
            )
            
            // JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 이전에)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정
     * 프론트엔드와의 통신을 위한 Cross-Origin 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 허용할 Origin (프론트엔드 URL)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",        // React 개발 서버 (개발용)
            "http://localhost:8080",        // 로컬 백엔드 (개발용)
            "http://my-po.store",          // 프로덕션 백엔드
            "https://my-po.store"          // 프로덕션 백엔드 (HTTPS)
        ));
        
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // 허용할 헤더
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // 노출할 헤더 (프론트엔드에서 읽을 수 있는 헤더)
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", "Location", "Content-Type"
        ));
        
        // 인증 정보 포함 허용 (쿠키, Authorization 헤더 등)
        configuration.setAllowCredentials(true);
        
        // 브라우저가 캐시할 시간 (초)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로에 CORS 설정 적용 (로그인 페이지 포함)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 