package com.example.egerdon.entity;


import com.example.egerdon.enums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 매물 등록의 루트 엔티티.
 * 거래 금액, 계약 상태, 등록자, 등록 시간 등의 핵심 정보만 포함한다.
 */
public class Listing {

    private Long id;  // 매물 고유 ID
    private Integer deposit;  // 보증금 (단위: 만원)
    private Integer monthlyRent;  // 월세 (단위: 만원, 전세는 0으로 설정)
    private Integer maintenanceFee;  // 관리비 (단위: 만원)
    private List<ListingImage> images;  // 연관된 이미지 목록
    private enums.ContractType contractType;  // 거래 유형 (월세, 전세, 반전세)
    private enums.ContractStatus contractStatus;  // 계약 가능 상태 (AVAILABLE, RESERVED, COMPLETED)
    private LocalDate availableFrom;  // 입주 가능일 (계약일과는 별개)
    private Long userId;  // 매물 등록자 ID (User 테이블 참조)
    private LocalDateTime createdAt;  // 등록 일시
    private LocalDateTime updatedAt;  // 마지막 수정 일시
}
