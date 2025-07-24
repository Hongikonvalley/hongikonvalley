package com.example.egerdon.dto.listing;

import com.example.egerdon.enums.ContractStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 매물 카드 정보 DTO (검색 결과용)
 */
@Getter
@Builder
public class ListingCardDto {
    
    private Long id; // 매물 ID
    private String title; // 매물 제목
    private String description; // 매물 소개
    private Integer deposit; // 보증금 (만원 단위)
    private Integer monthlyRent; // 월세 (만원 단위)
    private ContractStatus contractStatus; // 계약 상태
    private List<String> imageUrls; // 사진 URL 리스트
} 