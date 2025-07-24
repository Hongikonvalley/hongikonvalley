package com.example.egerdon.filter;

import com.example.egerdon.entity.User;
import com.example.egerdon.repository.UserRepository;
import com.example.egerdon.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT 인증 필터
 * Authorization 헤더에서 JWT 토큰을 추출하여 인증 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Authorization 헤더에서 JWT 토큰 추출
            String token = extractTokenFromRequest(request);
            
            // 토큰이 있고 유효한지 확인
            if (token != null && jwtUtil.validateToken(token)) {
                // 토큰에서 카카오 ID 추출
                String kakaoIdStr = jwtUtil.getKakaoIdFromToken(token);

                if (kakaoIdStr != null) {
                    try {
                        Long kakaoId = Long.valueOf(kakaoIdStr);
                        
                        // DB에서 사용자 조회
                        Optional<User> userOptional = userRepository.findByKakaoId(kakaoId);
                        
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            
                            // Authentication 객체 생성
                            UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                                );

                            // SecurityContext에 인증 정보 설정
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.debug("JWT 인증 성공: kakaoId={}, nickname={}", user.getKakaoId(), user.getNickname());
                        } else {
                            log.warn("토큰은 유효하지만 사용자를 찾을 수 없음: kakaoId={}", kakaoId);
                        }
                    } catch (NumberFormatException e) {
                        log.error("카카오 ID 형변환 실패: {}", kakaoIdStr);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JWT 인증 필터에서 오류 발생: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 JWT 토큰 추출
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        
        return null;
    }
} 