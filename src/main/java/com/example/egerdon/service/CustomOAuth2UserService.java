package com.example.egerdon.service;

import com.example.egerdon.entity.User;
import com.example.egerdon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.example.egerdon.enums.ListerType;

import java.util.Map;

/**
 * 커스텀 OAuth2 사용자 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        log.info("OAuth2 User attributes: {}", oAuth2User.getAttributes());
        
        // 카카오에서 제공하는 사용자 정보 추출
        Long kakaoId = Long.valueOf(oAuth2User.getName());
        Map<String, Object> profile = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        
        // 프로필 정보가 있는 경우 닉네임과 프로필 이미지 추출
        String tempNickname = "사용자";
        String tempProfileImageUrl = null;
        
        if (profile != null) {
            tempNickname = (String) profile.get("nickname");
            if (tempNickname == null || tempNickname.trim().isEmpty()) {
                tempNickname = "사용자" + kakaoId;
            }
            tempProfileImageUrl = (String) profile.get("profile_image");
        }
        
        final String nickname = tempNickname;
        final String profileImageUrl = tempProfileImageUrl;
        
        // 기존 사용자 조회 또는 새 사용자 생성
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> createNewUser(kakaoId, nickname, profileImageUrl));
        
        // 기존 사용자인 경우 프로필 정보 업데이트
        if (userRepository.existsByKakaoId(kakaoId)) {
            user.updateProfile(nickname, profileImageUrl);
            userRepository.save(user);
        }
        
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
    
    /**
     * 새 사용자 생성
     */
    private User createNewUser(Long kakaoId, String nickname, String profileImageUrl) {
        User newUser = User.builder()
                .kakaoId(kakaoId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .listerType(ListerType.UNKNOWN)
                .isActive(true)
                .build();
        
        return userRepository.save(newUser);
    }
} 