package com.example.egerdon.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

/**
 * 매물 이미지 엔티티.
 * 하나의 매물(Listing)에 여러 장의 이미지가 연결될 수 있으며, 대표 이미지 여부도 표시한다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    private String imageUrl;
    private LocalDateTime uploadedAt;
}


