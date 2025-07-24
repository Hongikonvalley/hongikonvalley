package com.example.egerdon.service;

import com.example.egerdon.dto.listing.ListingCardDto;
import com.example.egerdon.dto.listing.ListingSearchRequest;
import com.example.egerdon.dto.listing.ListingSearchResponse;
import com.example.egerdon.entity.Listing;
import com.example.egerdon.entity.ListingImage;
import com.example.egerdon.repository.ListingImageRepository;
import com.example.egerdon.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 매물 검색 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListingSearchService {
    
    private final ListingRepository listingRepository;
    private final ListingImageRepository listingImageRepository;
    
    /**
     * 매물 검색
     * 
     * @param request 검색 조건
     * @return 검색 결과
     */
    public ListingSearchResponse searchListings(ListingSearchRequest request) {
        log.info("매물 검색 시작 - 조건: 목적={}, 동네={}, 예산={}만원, 희망입주일={}", 
                request.getPurpose(), request.getNeighborhood(), 
                request.getBudget(), request.getDesiredMoveInDate());
        
        // 1. 검색 조건 전처리
        String neighborhood = preprocessNeighborhood(request.getNeighborhood());
        Integer maxBudget = request.getBudget();
        
        log.info("🔍 검색 조건 상세 - 원본 동네: '{}', 전처리된 동네: '{}', 최대예산: {}만원", 
                request.getNeighborhood(), neighborhood, maxBudget);
        
        // 2. 페이징 설정
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        
        // 3. 매물 검색
        Page<Listing> listingPage = listingRepository.findBySearchConditions(
            neighborhood, maxBudget, request.getDesiredMoveInDate(), pageable
        );
        
        // 4. 총 개수 조회 (정확한 카운트를 위해 별도 쿼리)
        Long totalCount = listingRepository.countBySearchConditions(
            neighborhood, maxBudget, request.getDesiredMoveInDate()
        );
        
        // 5. 매물 이미지 정보 조회
        List<Long> listingIds = listingPage.getContent().stream()
            .map(Listing::getId)
            .toList();
        
        Map<Long, List<String>> listingImageMap = getListingImageMap(listingIds);
        
        // 6. DTO 변환
        List<ListingCardDto> listingCards = listingPage.getContent().stream()
            .map(listing -> convertToListingCardDto(listing, listingImageMap))
            .toList();
        
        log.info("매물 검색 완료 - 총 {}건 중 {}건 조회 (페이지: {}/{})", 
                totalCount, listingCards.size(), request.getPage() + 1, listingPage.getTotalPages());
        
        return ListingSearchResponse.builder()
            .totalCount(totalCount)
            .listings(listingCards)
            .currentPage(request.getPage())
            .totalPages(listingPage.getTotalPages())
            .hasNext(listingPage.hasNext())
            .build();
    }
    
    /**
     * 동네명 전처리
     */
    private String preprocessNeighborhood(String neighborhood) {
        if (neighborhood == null || neighborhood.trim().isEmpty()) {
            return null;
        }
        
        // 동네명에서 불필요한 단어 제거 및 정규화
        return neighborhood.trim()
            .replace("동", "")  // "상수동" -> "상수"로 변환하여 더 넓은 검색
            .replace(" ", "");  // 공백 제거
    }
    
    /**
     * 매물별 이미지 맵 생성
     */
    private Map<Long, List<String>> getListingImageMap(List<Long> listingIds) {
        if (listingIds.isEmpty()) {
            return Map.of();
        }
        
        List<ListingImage> images = listingImageRepository.findByListingIdInOrderByListingIdAscUploadedAtAsc(listingIds);
        
        return images.stream()
            .collect(Collectors.groupingBy(
                image -> image.getListing().getId(),
                Collectors.mapping(ListingImage::getImageUrl, Collectors.toList())
            ));
    }
    
    /**
     * Listing 엔티티를 ListingCardDto로 변환
     */
    private ListingCardDto convertToListingCardDto(Listing listing, Map<Long, List<String>> imageMap) {
        List<String> imageUrls = imageMap.getOrDefault(listing.getId(), List.of());
        
        return ListingCardDto.builder()
            .id(listing.getId())
            .title(listing.getTitle())
            .description(listing.getDescription())
            .deposit(listing.getDeposit())
            .monthlyRent(listing.getMonthlyRent())
            .contractStatus(listing.getContractStatus())
            .imageUrls(imageUrls)
            .build();
    }
} 