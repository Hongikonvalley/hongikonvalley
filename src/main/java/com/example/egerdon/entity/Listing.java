package com.example.egerdon.entity;


import com.example.egerdon.enums.ContractStatus;
import com.example.egerdon.enums.ContractType;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 매물 등록의 루트 엔티티.
 * 거래 금액, 계약 상태, 등록자, 등록 시간 등의 핵심 정보만 포함한다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Listing {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer deposit;
    private Integer monthlyRent;
    private Integer maintenanceFee;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    private LocalDate availableFrom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

