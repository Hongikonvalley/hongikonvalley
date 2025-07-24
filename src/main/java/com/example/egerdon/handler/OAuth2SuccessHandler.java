package com.example.egerdon.handler;

import com.example.egerdon.service.PrincipalDetails;
import com.example.egerdon.entity.User;
import com.example.egerdon.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 로그인 성공 핸들러
 * JWT 토큰 생성 후 프론트엔드로 리다이렉트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    
    // 프론트엔드 콜백 URL
    private static final String FRONTEND_CALLBACK_URL = "http://localhost:3000/auth/callback";

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        try {
            // OAuth2 인증 정보에서 사용자 정보 추출
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            User user = principalDetails.getUser();

            // JWT 토큰 생성
            String accessToken = jwtUtil.generateAccessToken(String.valueOf(user.getKakaoId()), "USER");
            String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getKakaoId()));

            log.info("OAuth2 로그인 성공 - 사용자: kakaoId={}, nickname={}, JWT 토큰 발급 완료", 
                    user.getKakaoId(), user.getNickname());

            // 프론트엔드 콜백 URL로 리다이렉트
            String redirectUrl = buildCallbackUrl(accessToken, refreshToken, user);
            
            log.info("프론트엔드 콜백 URL로 리다이렉트: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 로그인 성공 처리 중 오류 발생: {}", e.getMessage(), e);
            
            // 에러 발생 시 프론트엔드 에러 페이지로 리다이렉트
            String errorUrl = FRONTEND_CALLBACK_URL + "?error=login_failed&message=" + 
                              URLEncoder.encode("로그인 처리 중 오류가 발생했습니다.", StandardCharsets.UTF_8);
            response.sendRedirect(errorUrl);
        }
    }

    /**
     * 프론트엔드 콜백 URL 생성
     */
    private String buildCallbackUrl(String accessToken, String refreshToken, User user) {
        try {
            StringBuilder urlBuilder = new StringBuilder(FRONTEND_CALLBACK_URL);
            urlBuilder.append("?");
            
            // JWT 토큰들
            urlBuilder.append("access_token=").append(URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
            urlBuilder.append("&refresh_token=").append(URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
            urlBuilder.append("&token_type=Bearer");
            
            // 사용자 정보
            urlBuilder.append("&kakao_id=").append(URLEncoder.encode(String.valueOf(user.getKakaoId()), StandardCharsets.UTF_8));
            urlBuilder.append("&nickname=").append(URLEncoder.encode(user.getNickname(), StandardCharsets.UTF_8));
            urlBuilder.append("&lister_type=").append(URLEncoder.encode(user.getListerType().name(), StandardCharsets.UTF_8));
            
            return urlBuilder.toString();
            
        } catch (Exception e) {
            log.error("콜백 URL 생성 중 오류 발생: {}", e.getMessage(), e);
            return FRONTEND_CALLBACK_URL + "?error=url_generation_failed";
        }
    }
} 