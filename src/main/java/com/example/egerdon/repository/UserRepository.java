package com.example.egerdon.repository;

import com.example.egerdon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 카카오 ID로 사용자 조회
     */
    Optional<User> findByKakaoId(Long kakaoId);
    
    /**
     * 카카오 ID 존재 여부 확인
     */
    boolean existsByKakaoId(Long kakaoId);
} 