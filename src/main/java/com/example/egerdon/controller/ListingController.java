package com.example.egerdon.controller;

import com.example.egerdon.dto.CommonResponse;
import com.example.egerdon.dto.listing.ListingSearchRequest;
import com.example.egerdon.dto.listing.ListingSearchResponse;
import com.example.egerdon.entity.Listing;
import com.example.egerdon.repository.ListingRepository;
import com.example.egerdon.service.ListingSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 매물 관련 REST API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingController {
    
    private final ListingSearchService listingSearchService;
    private final ListingRepository listingRepository;
    
    /**
     * 디버깅용: 전체 매물 조회 API
     */
    @GetMapping("/debug/all")
    public ResponseEntity<CommonResponse<Object>> getAllListingsForDebug() {
        log.info("디버깅용 전체 매물 조회 API 호출");
        
        // 전체 매물 개수
        long totalCount = listingRepository.count();
        log.info("총 매물 개수: {}", totalCount);
        
        // contract_status별 개수
        List<Object[]> statusCounts = listingRepository.countByContractStatus();
        log.info("Contract Status별 개수:");
        for (Object[] row : statusCounts) {
            log.info("  - {}: {}건", row[0], row[1]);
        }
        
        // 첫 5개 매물 조회
        Page<Listing> listings = listingRepository.findAllListingsForDebug(PageRequest.of(0, 5));
        log.info("첫 5개 매물 조회 결과: {}건", listings.getContent().size());
        
        for (Listing listing : listings.getContent()) {
            log.info("매물 ID: {}, 제목: {}, Contract Status: {}, 보증금: {}", 
                    listing.getId(), listing.getTitle(), listing.getContractStatus(), listing.getDeposit());
        }
        
        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(200)
                .message("디버깅 정보 조회 완료")
                .data(java.util.Map.of(
                        "totalCount", totalCount,
                        "statusCounts", statusCounts,
                        "sampleListings", listings.getContent()
                ))
                .build());
    }
    
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