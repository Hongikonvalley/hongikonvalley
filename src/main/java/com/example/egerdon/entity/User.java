package com.example.egerdon.entity;

import com.example.egerdon.enums;

import java.time.LocalDateTime;

public class User {

    private Long id;  // 시스템 고유 사용자 ID (PK)
    private Long kakaoId;  // 카카오 OAuth 식별자 (v2 API 기준 sub 또는 id)
    private String email;  // 사용자 이메일 (동의 시에만 저장)
    private String nickname;  // 공개 닉네임 (매물 등록자 표시용)
    private String profileImageUrl;  // 프로필 이미지 URL (카카오 프로필 이미지)
    private enums.ListerType listerType;  // 등록자 유형 (집주인, 세입자, 중개사 등)
    private Boolean isActive;  // 탈퇴 여부 (soft delete 대응)
    private LocalDateTime createdAt;  // 가입 시각
    private LocalDateTime updatedAt;  // 최종 정보 수정 시각
}