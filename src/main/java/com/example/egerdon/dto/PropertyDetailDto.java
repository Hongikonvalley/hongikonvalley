package com.example.egerdon.dto;

import com.example.egerdon.enums.LayoutType;
import com.example.egerdon.enums.PropertyType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDetailDto {
    private Long id;                   // 식별자
    private Long listingId;            // 연관된 Listing ID

    private PropertyType propertyType;
    private LayoutType layoutType;

    private Double exclusiveArea;
    private Integer bathroomCount;
    private Short floorNumber;
    private Short totalFloors;
    private LocalDate builtAt;
    private Short householdCount;
}

