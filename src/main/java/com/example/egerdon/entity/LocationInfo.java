package com.example.egerdon.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;


/**
 * 위치 기반 정보 서브 도메인.
 * 주소, 상세주소, 인근 지하철역, 위도/경도, 주변 시설 등을 포함한다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    private String address;
    private String detailAddress;

    @ElementCollection
    private List<String> nearbySubwayStations;

    @ElementCollection
    private List<String> nearbyFacilities;

    private Double latitude;
    private Double longitude;
}

