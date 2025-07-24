package com.example.egerdon.controller;

import com.example.egerdon.dto.CommonResponse;
import com.example.egerdon.dto.listing.ListingSearchRequest;
import com.example.egerdon.dto.listing.ListingSearchResponse;
import com.example.egerdon.service.ListingSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 매물 관련 REST API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingController {
    
    private final ListingSearchService listingSearchService;
    
    /**
     * 매물 검색 API
     * 
     * @param purpose 목적 (예: "학교", "직장")
     * @param neighborhood 동네 (예: "상수동")
     * @param budget 예산 (만원 단위, 예: 80)
     * @param desiredMoveInDate 입주 희망일 (yyyy-MM-dd 형식)
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 20)
     * @return 검색 결과
     */
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<ListingSearchResponse>> searchListings(
            @RequestParam(required = false) String purpose,
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) Integer budget,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desiredMoveInDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        log.info("매물 검색 API 호출 - 목적: {}, 동네: {}, 예산: {}만원, 입주일: {}, 페이지: {}/{}", 
                purpose, neighborhood, budget, desiredMoveInDate, page, size);
        
        // 요청 DTO 생성
        ListingSearchRequest request = ListingSearchRequest.builder()
                .purpose(purpose)
                .neighborhood(neighborhood)
                .budget(budget)
                .desiredMoveInDate(desiredMoveInDate)
                .page(page)
                .size(size)
                .build();
        
        // 서비스 호출
        ListingSearchResponse response = listingSearchService.searchListings(request);
        
        // 응답 생성
        CommonResponse<ListingSearchResponse> commonResponse = CommonResponse.<ListingSearchResponse>builder()
                .statusCode(200)
                .message("검색 성공")
                .data(response)
                .build();
        
        log.info("매물 검색 API 응답 - 총 {}건 매물 조회", response.getTotalCount());
        
        return ResponseEntity.ok(commonResponse);
    }
} 