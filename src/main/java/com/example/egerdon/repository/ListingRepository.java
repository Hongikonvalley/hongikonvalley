package com.example.egerdon.repository;

import com.example.egerdon.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    // 필요하면 커스텀 메소드 추가
}
