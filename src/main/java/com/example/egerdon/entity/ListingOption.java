package com.example.egerdon.entity;

import com.example.egerdon.enums;

/**
 * 매물의 주거 편의 조건에 대한 서브 도메인.
 * 주차, 엘리베이터, 냉난방, 방음, 현관 유형 등 실거주와 밀접한 옵션을 관리한다.
 */
public class ListingOption {

    private Long id;
    private Long listingId;  // 해당 매물의 ID (FK)
    private Boolean parkingAvailable;  // 주차 가능 여부
    private Boolean hasElevator;  // 엘리베이터 유무
    private enums.EntranceType entranceType;  // 현관 구조 (공동출입구, 복도형 등)
    private enums.Orientation orientation;  // 주실 방향 (남향, 북향 등)
    private enums.HeatingType heatingType;  // 냉난방 유형 (개별난방, 중앙난방 등)
    private enums.SoundproofLevel soundproofLevel;  // 방음 수준 (우수, 일반, 불량)
}
