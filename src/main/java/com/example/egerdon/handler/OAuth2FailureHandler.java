package com.example.egerdon.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 로그인 실패 핸들러
 * 에러 정보와 함께 프론트엔드로 리다이렉트
 */
@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // 프론트엔드 콜백 URL (에러용)
    private static final String FRONTEND_CALLBACK_URL = "http://localhost:3000/auth/callback";

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        // 실패 원인 로깅
        log.error("OAuth2 로그인 실패: {}", exception.getMessage());

        // 에러 메시지 준비
        String errorMessage = getErrorMessage(exception);
        
        // 프론트엔드 콜백 URL로 에러 정보와 함께 리다이렉트
        String errorUrl = buildErrorCallbackUrl(errorMessage, exception.getClass().getSimpleName());
        
        log.info("OAuth2 로그인 실패 - 프론트엔드 에러 콜백 URL로 리다이렉트: {}", errorUrl);
        response.sendRedirect(errorUrl);
    }

    /**
     * 에러 유형별 메시지 매핑
     */
    private String getErrorMessage(AuthenticationException exception) {
        String exceptionName = exception.getClass().getSimpleName();
        
        return switch (exceptionName) {
            case "OAuth2AuthenticationException" -> "카카오 로그인 인증에 실패했습니다.";
            case "InsufficientAuthenticationException" -> "인증 정보가 부족합니다.";
            case "AuthenticationCredentialsNotFoundException" -> "인증 자격 증명을 찾을 수 없습니다.";
            case "BadCredentialsException" -> "잘못된 인증 정보입니다.";
            default -> "로그인 중 알 수 없는 오류가 발생했습니다.";
        };
    }

    /**
     * 에러 콜백 URL 생성
     */
    private String buildErrorCallbackUrl(String errorMessage, String errorType) {
        try {
            StringBuilder urlBuilder = new StringBuilder(FRONTEND_CALLBACK_URL);
            urlBuilder.append("?");
            
            // 에러 정보
            urlBuilder.append("error=true");
            urlBuilder.append("&error_type=").append(URLEncoder.encode(errorType, StandardCharsets.UTF_8));
            urlBuilder.append("&error_message=").append(URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
            urlBuilder.append("&timestamp=").append(System.currentTimeMillis());
            
            return urlBuilder.toString();
            
        } catch (Exception e) {
            log.error("에러 콜백 URL 생성 중 오류 발생: {}", e.getMessage(), e);
            return FRONTEND_CALLBACK_URL + "?error=true&error_type=unknown&error_message=URL+generation+failed";
        }
    }
} 