package com.example.egerdon.dto.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * 토큰 검증 응답 DTO
 */
@Getter
@Builder
public class TokenValidationResponse {
    private boolean valid;
    private String userKakaoId;
    private List<String> authorities;
    private boolean authenticated;

    /**
     * 토큰 검증 결과를 TokenValidationResponse로 변환하는 정적 팩토리 메서드
     */
    public static TokenValidationResponse from(String kakaoId, Collection<? extends GrantedAuthority> authorities) {
        return TokenValidationResponse.builder()
                .valid(true)
                .userKakaoId(kakaoId)
                .authorities(authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .authenticated(true)
                .build();
    }
} 