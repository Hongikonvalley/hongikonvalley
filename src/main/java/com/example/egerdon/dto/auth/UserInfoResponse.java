package com.example.egerdon.dto.auth;

import com.example.egerdon.entity.User;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 정보 응답 DTO
 */
@Getter
@Builder
public class UserInfoResponse {
    private String kakaoId;
    private String nickname;
    private String profileImageUrl;
    private String listerType;
    private boolean isActive;

    /**
     * User 엔티티를 UserInfoResponse로 변환하는 정적 팩토리 메서드
     */
    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .kakaoId(String.valueOf(user.getKakaoId()))
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .listerType(user.getListerType().name())
                .isActive(user.getIsActive())
                .build();
    }
} 