package com.fastcampus.housebatch.core.entity;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "apt_deal")
public class AptDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptDealId;

    @ManyToOne
    @JoinColumn(name = "apt_id", nullable = false)
    @ToString.Exclude
    private Apt apt;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal exclusiveArea;

    @Column(nullable = false)
    private LocalDate dealDate;

    @Column(nullable = false)
    private Long dealAmount;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private Boolean dealCanceled = false;

    @Column
    private LocalDate dealCanceledDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public AptDeal(Apt apt, BigDecimal exclusiveArea, LocalDate dealDate, Long dealAmount, Integer floor, Boolean dealCanceled, LocalDate dealCanceledDate) {
        this.apt = apt;
        this.exclusiveArea = exclusiveArea;
        this.dealDate = dealDate;
        this.dealAmount = dealAmount;
        this.floor = floor;
        this.dealCanceled = dealCanceled;
        this.dealCanceledDate = dealCanceledDate;
    }

    public static AptDeal of(AptDealDto dto, Apt apt) {
        return AptDeal.builder()
                .apt(apt)
                .exclusiveArea(dto.getExclusiveArea())
                .dealDate(dto.getDealDate())
                .dealAmount(dto.getDealAmount())
                .floor(dto.getFloor())
                .dealCanceled(dto.isDealCanceled())
                .dealCanceledDate(dto.getDealCanceledDate())
                .build();
    }

    public void setDealCanceled(boolean dealCanceled, LocalDate dealCanceledDate) {
        this.dealCanceled = dealCanceled;
        this.dealCanceledDate = dealCanceledDate;
    }
}
