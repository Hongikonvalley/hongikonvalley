package com.example.egerdon.repository;

import com.example.egerdon.entity.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * 매물 Repository
 */
@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    /**
     * 디버깅용: 모든 매물 조회 (조건 없음)
     */
    @Query("SELECT l FROM Listing l LEFT JOIN FETCH l.user ORDER BY l.createdAt DESC")
    Page<Listing> findAllListingsForDebug(Pageable pageable);

    /**
     * 디버깅용: contract_status별 매물 개수 조회
     */
    @Query("SELECT l.contractStatus, COUNT(l) FROM Listing l GROUP BY l.contractStatus")
    java.util.List<Object[]> countByContractStatus();

    /**
     * 동네와 예산 범위로 매물 검색
     *
     * @param neighborhood 동네명 (주소에 포함된 경우)
     * @param maxBudget 최대 예산 (보증금 기준, 만원 단위)
     * @param availableDate 입주 가능일 (이 날짜 이전 또는 이 날짜까지 입주 가능한 매물)
     * @param pageable 페이징 정보
     * @return 조건에 맞는 매물 페이지
     */
    @Query("""
        SELECT DISTINCT l FROM Listing l
        LEFT JOIN FETCH l.user
        LEFT JOIN LocationInfo loc ON l.id = loc.listing.id
        WHERE (:neighborhood IS NULL OR loc.address LIKE %:neighborhood%)
        AND (:maxBudget IS NULL OR l.deposit <= :maxBudget)
        AND (:availableDate IS NULL OR l.availableFrom <= :availableDate)
        AND l.contractStatus = com.example.egerdon.enums.ContractStatus.AVAILABLE
        ORDER BY l.createdAt DESC
        """)
    Page<Listing> findBySearchConditions(
        @Param("neighborhood") String neighborhood,
        @Param("maxBudget") Integer maxBudget,
        @Param("availableDate") LocalDate availableDate,
        Pageable pageable
    );

    /**
     * 검색 조건에 맞는 매물 총 개수 조회
     */
    @Query("""
        SELECT COUNT(DISTINCT l) FROM Listing l
        LEFT JOIN LocationInfo loc ON l.id = loc.listing.id
        WHERE (:neighborhood IS NULL OR loc.address LIKE %:neighborhood%)
        AND (:maxBudget IS NULL OR l.deposit <= :maxBudget)
        AND (:availableDate IS NULL OR l.availableFrom <= :availableDate)
        AND l.contractStatus = com.example.egerdon.enums.ContractStatus.AVAILABLE
        """)
    Long countBySearchConditions(
        @Param("neighborhood") String neighborhood,
        @Param("maxBudget") Integer maxBudget,
        @Param("availableDate") LocalDate availableDate
    );
}