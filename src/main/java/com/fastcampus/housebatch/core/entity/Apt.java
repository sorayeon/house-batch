package com.fastcampus.housebatch.core.entity;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "apt")
public class Apt extends BaseTimeEntity {
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

    @Builder
    public Apt(String aptName, String guLawdCd, String jibun, String dong, Integer builtYear) {
        this.aptName = aptName;
        this.guLawdCd = guLawdCd;
        this.jibun = jibun;
        this.dong = dong;
        this.builtYear = builtYear;
    }

    public static Apt from(AptDealDto dto) {
        return Apt.builder()
                .aptName(dto.getAptName().trim())
                .guLawdCd(dto.getRegionalCode().trim())
                .jibun(dto.getJibun().trim())
                .dong(dto.getDong().trim())
                .builtYear(dto.getBuiltYear())
                .build();
    }
}
