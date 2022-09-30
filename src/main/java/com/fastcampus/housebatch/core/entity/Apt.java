package com.fastcampus.housebatch.core.entity;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "apt")
public class Apt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptId;

    @Column(nullable = false)
    private String aptName;

    @Column(nullable = false)
    private String guLawdCd;

    @Column(nullable = false)
    private String jibun;

    @Column(nullable = false)
    private String dong;

    @Column(nullable = false)
    private Integer builtYear;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Apt(String aptName, String guLawdCd, String jibun, String dong, Integer builtYear) {
        this.aptName = aptName;
        this.guLawdCd = guLawdCd;
        this.jibun = jibun;
        this.dong = dong;
        this.builtYear = builtYear;
    }

    public static Apt of(AptDealDto dto) {
        return Apt.builder()
                .aptName(dto.getAptName().trim())
                .guLawdCd(dto.getRegionalCode().trim())
                .jibun(dto.getJibun().trim())
                .dong(dto.getDong().trim())
                .builtYear(dto.getBuiltYear())
                .build();
    }
}
