package com.example.egerdon.repository;

import com.example.egerdon.entity.ListingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 매물 이미지 Repository
 */
@Repository
public interface ListingImageRepository extends JpaRepository<ListingImage, Long> {
    
    /**
     * 매물 ID로 이미지 목록 조회
     * 
     * @param listingId 매물 ID
     * @return 해당 매물의 이미지 목록 (업로드 시간 순)
     */
    List<ListingImage> findByListingIdOrderByUploadedAtAsc(Long listingId);
    
    /**
     * 여러 매물 ID로 이미지 목록 조회 (매물 검색 결과용)
     * 
     * @param listingIds 매물 ID 목록
     * @return 해당 매물들의 이미지 목록
     */
    List<ListingImage> findByListingIdInOrderByListingIdAscUploadedAtAsc(List<Long> listingIds);
} 