package com.example.egerdon.entity;

import java.util.List;

/**
 * 위치 기반 정보 서브 도메인.
 * 주소, 상세주소, 인근 지하철역, 위도/경도, 주변 시설 등을 포함한다.
 */
public class LocationInfo {

    private Long id;
    private Long listingId;  // 해당 매물의 ID (FK)
    private String address;  // 도로명 주소 (예: 서울시 마포구 와우산로 29가길 10)
    private String detailAddress;  // 상세 주소 (예: 302호)
    private List<String> nearbySubwayStations;  // 인근 지하철역 리스트 (예: 홍대입구역, 합정역 등)
    private List<String> nearbyFacilities;  // 주변 편의시설 리스트 (예: 편의점, 카페, 병원 등)
    private Double latitude;  // 위도 (지도용)
    private Double longitude;  // 경도 (지도용)
}

