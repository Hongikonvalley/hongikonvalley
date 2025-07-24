package com.example.egerdon.entity;

import java.time.LocalDateTime;

/**
 * 매물 이미지 엔티티.
 * 하나의 매물(Listing)에 여러 장의 이미지가 연결될 수 있으며, 대표 이미지 여부도 표시한다.
 */
public class ListingImage {

    private Long id;  // 이미지 고유 식별자 (PK)
    private Listing listing;  // 매물 ID (FK)
    private String imageUrl;  // 이미지 파일 URL
    private LocalDateTime uploadedAt;  // 업로드 시간
}

