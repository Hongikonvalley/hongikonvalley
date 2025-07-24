package com.example.egerdon.dto.listing;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 매물 검색 응답 DTO
 */
@Getter
@Builder
public class ListingSearchResponse {
    
    private Long totalCount; // 조건에 맞는 매물 총 개수
    private List<ListingCardDto> listings; // 매물 목록
    private Integer currentPage; // 현재 페이지 번호
    private Integer totalPages; // 총 페이지 수
    private Boolean hasNext; // 다음 페이지 존재 여부
} 