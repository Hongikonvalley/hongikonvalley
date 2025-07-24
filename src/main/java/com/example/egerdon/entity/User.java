package com.example.egerdon.entity;

import com.example.egerdon.enums.ListerType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 사용자 엔티티
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long kakaoId; // 카카오 OAuth 식별자

    @Column(nullable = false)
    private String nickname; // 공개 닉네임

    @Column
    private String profileImageUrl; // 프로필 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListerType listerType; // 등록자 유형

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true; // 활성 상태

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 가입 시각

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 최종 수정 시각

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 프로필 정보 업데이트
     */
    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * 등록자 유형 업데이트
     */
    public void updateListerType(ListerType listerType) {
        this.listerType = listerType;
    }

    /**
     * 계정 비활성화
     */
    public void deactivate() {
        this.isActive = false;
    }

    // === UserDetails 인터페이스 구현 ===

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return ""; // OAuth2 사용자는 비밀번호 없음
    }

    @Override
    public String getUsername() {
        return String.valueOf(kakaoId); // 카카오 ID가 사용자 식별자
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}