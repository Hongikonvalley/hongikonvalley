package com.example.egerdon.entity;

import com.example.egerdon.enums;

import java.time.LocalDate;

/**
 * 부동산 물리적 정보에 대한 서브 도메인.
 * 건물 구조, 층수, 면적, 준공일 등의 고정된 속성을 담당한다.
 */
public class PropertyDetail {

    private Long id;
    private Long listingId;  // 해당 매물의 ID (FK)
    private enums.PropertyType propertyType;  // 매물 유형 (오피스텔, 빌라, 아파트 등)
    private enums.LayoutType layoutType;  // 방 구조 유형 (오픈형, 분리형, 복층형)
    private Double exclusiveArea;  // 전용 면적 (제곱미터 단위)
    private Integer bathroomCount;  // 욕실 수
    private Short floorNumber;  // 해당 세대의 층수 (예: 3)
    private Short totalFloors;  // 건물 전체 층수 (예: 5)
    private LocalDate builtAt;  // 건물 준공일
    private Short householdCount;  // 전체 세대 수 (예: 12세대 빌라)
}

