package com.example.egerdon.dto.listing;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * 매물 검색 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingSearchRequest {
    
    private String purpose; // 목적 (예: "학교", "직장")
    private String neighborhood; // 동네 (예: "상수동")
    private String budget; // 예산 (예: "1억 2천만원")
    private LocalDate desiredMoveInDate; // 기간 - 입주 희망일 (yyyy-MM-dd 형식)
    
    @Builder.Default
    private Integer page = 0; // 페이지 번호 (기본값: 0)
    
    @Builder.Default
    private Integer size = 20; // 페이지 크기 (기본값: 20)
} 