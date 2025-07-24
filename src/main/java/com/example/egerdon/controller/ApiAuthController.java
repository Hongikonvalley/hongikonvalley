package com.example.egerdon.controller;

import com.example.egerdon.dto.CommonResponse;
import com.example.egerdon.dto.auth.TokenValidationResponse;
import com.example.egerdon.dto.auth.UserInfoResponse;
import com.example.egerdon.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API용 인증 관련 컨트롤러
 * JWT 토큰 기반 사용자 정보 제공
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    /**
     * 현재 로그인한 사용자 정보 조회
     * JWT 토큰 검증 후 사용자 정보 반환
     */
    @GetMapping("/me")
    public ResponseEntity<CommonResponse<UserInfoResponse>> getCurrentUser(
            @AuthenticationPrincipal User user
    ) {
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(CommonResponse.<UserInfoResponse>builder()
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .message("인증이 필요합니다.")
                                .data(null)
                                .build());
            }

            UserInfoResponse userInfo = UserInfoResponse.from(user);

            log.info("사용자 정보 조회: kakaoId={}, nickname={}", user.getKakaoId(), user.getNickname());
            
            return ResponseEntity.ok(
                CommonResponse.<UserInfoResponse>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("사용자 정보 조회를 성공했습니다.")
                    .data(userInfo)
                    .build()
            );

        } catch (Exception e) {
            log.error("사용자 정보 조회 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.<UserInfoResponse>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("사용자 정보 조회에 실패했습니다.")
                            .data(null)
                            .build());
        }
    }

    /**
     * JWT 토큰 검증
     * 토큰의 유효성 확인 및 사용자 권한 정보 제공
     */
    @GetMapping("/validate")
    public ResponseEntity<CommonResponse<TokenValidationResponse>> validateToken(
            @AuthenticationPrincipal User user
    ) {
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(CommonResponse.<TokenValidationResponse>builder()
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .message("유효하지 않은 토큰입니다.")
                                .data(null)
                                .build());
            }

            TokenValidationResponse tokenInfo = TokenValidationResponse.from(
                String.valueOf(user.getKakaoId()), 
                user.getAuthorities()
            );

            return ResponseEntity.ok(
                CommonResponse.<TokenValidationResponse>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("토큰이 유효합니다.")
                    .data(tokenInfo)
                    .build()
            );

        } catch (Exception e) {
            log.error("토큰 검증 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.<TokenValidationResponse>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("토큰 검증에 실패했습니다.")
                            .data(null)
                            .build());
        }
    }

    /**
     * 로그아웃 안내
     * JWT 토큰은 서버에서 무효화할 수 없으므로 클라이언트 측 처리 안내
     */
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout() {
        String logoutGuide = "JWT 토큰은 서버에서 무효화할 수 없습니다. " +
                           "클라이언트에서 저장된 토큰을 삭제해주세요. " +
                           "(로컬스토리지, 세션스토리지, 메모리 등)";
        
        return ResponseEntity.ok(
            CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그아웃 처리 안내입니다.")
                .data(logoutGuide)
                .build()
        );
    }
} 