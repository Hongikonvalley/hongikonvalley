package com.example.egerdon.mapper;

import com.example.egerdon.dto.PropertyDetailDto;
import com.example.egerdon.entity.PropertyDetail;
import com.example.egerdon.entity.Listing;

public class PropertyDetailMapper {

    public static PropertyDetailDto toDto(PropertyDetail entity) {
        return PropertyDetailDto.builder()
                .id(entity.getId())
                .listingId(entity.getListing().getId())
                .propertyType(entity.getPropertyType())
                .layoutType(entity.getLayoutType())
                .exclusiveArea(entity.getExclusiveArea())
                .bathroomCount(entity.getBathroomCount())
                .floorNumber(entity.getFloorNumber())
                .totalFloors(entity.getTotalFloors())
                .builtAt(entity.getBuiltAt())
                .householdCount(entity.getHouseholdCount())
                .build();
    }

    public static PropertyDetail toEntity(PropertyDetailDto dto, Listing listing) {
        return PropertyDetail.builder()
                .id(dto.getId())
                .listing(listing) // listing은 Service 계층에서 조회 후 주입
                .propertyType(dto.getPropertyType())
                .layoutType(dto.getLayoutType())
                .exclusiveArea(dto.getExclusiveArea())
                .bathroomCount(dto.getBathroomCount())
                .floorNumber(dto.getFloorNumber())
                .totalFloors(dto.getTotalFloors())
                .builtAt(dto.getBuiltAt())
                .householdCount(dto.getHouseholdCount())
                .build();
    }
}
