package com.example.egerdon.service;

import com.example.egerdon.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * OAuth2 인증된 사용자 정보
 */
@Getter
public class PrincipalDetails implements OAuth2User {
    
    private final User user;
    private final Map<String, Object> attributes;
    
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
    @Override
    public String getName() {
        return String.valueOf(user.getKakaoId());
    }
    
    /**
     * 사용자 ID 반환
     */
    public Long getUserId() {
        return user.getId();
    }
    
    /**
     * 닉네임 반환
     */
    public String getNickname() {
        return user.getNickname();
    }
} 