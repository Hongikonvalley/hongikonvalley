package com.example.egerdon.entity;

import java.time.LocalDate;

import com.example.egerdon.enums.LayoutType;
import com.example.egerdon.enums.PropertyType;
import jakarta.persistence.*;
import lombok.*;

/**
 * 부동산 물리적 정보에 대한 서브 도메인.
 * 건물 구조, 층수, 면적, 준공일 등의 고정된 속성을 담당한다.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private LayoutType layoutType;

    private Double exclusiveArea;
    private Integer bathroomCount;
    private Short floorNumber;
    private Short totalFloors;
    private LocalDate builtAt;
    private Short householdCount;
}

