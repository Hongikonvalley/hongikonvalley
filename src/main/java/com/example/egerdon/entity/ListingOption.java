package com.example.egerdon.entity;

import com.example.egerdon.enums.EntranceType;
import com.example.egerdon.enums.HeatingType;
import com.example.egerdon.enums.Orientation;
import com.example.egerdon.enums.SoundproofLevel;
import jakarta.persistence.*;
import lombok.*;
/**
 * 매물의 주거 편의 조건에 대한 서브 도메인.
 * 주차, 엘리베이터, 냉난방, 방음, 현관 유형 등 실거주와 밀접한 옵션을 관리한다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    private Boolean parkingAvailable;
    private Boolean hasElevator;

    @Enumerated(EnumType.STRING)
    private EntranceType entranceType;

    @Enumerated(EnumType.STRING)
    private Orientation orientation;

    @Enumerated(EnumType.STRING)
    private HeatingType heatingType;

    @Enumerated(EnumType.STRING)
    private SoundproofLevel soundproofLevel;
}

