package com.fastcampus.housebatch.core.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 아파트 실거래가 API 의 각각의 거래 정보를 담는 객체
 */
@ToString
@Getter
@XmlRootElement(name = "item")
public class AptDealDto {

    @XmlElement(name = "거래금액")
    private String dealAmount;

    @XmlElement(name = "건축년도")
    private Integer builtYear;

    @XmlElement(name = "년")
    private Integer year;

    @XmlElement(name = "법정동")
    private String dong;

    @XmlElement(name = "아파트")
    private String aptName;

    @XmlElement(name = "월")
    private Integer month;

    @XmlElement(name = "일")
    private Integer day;

    @XmlElement(name = "전용면적")
    private BigDecimal exclusiveArea;

    @XmlElement(name = "지번")
    private String jibun;

    @XmlElement(name = "지역코드")
    private String regionalCode;

    @XmlElement(name = "층")
    private Integer floor;

    @XmlElement(name = "해제사유발생일")
    private String dealCanceledDate; // 21.07.30

    @XmlElement(name = "해제여부")
    private String dealCanceled; // O

    public Long getDealAmount() {
        return Long.parseLong(dealAmount.replaceAll(",", "").trim());
    }

    // 취소된 계약은 O로 리턴
    public boolean isDealCanceled() {
        return "O".equals(dealCanceled.trim());
    }

    // 취소된 계약일자 yy.MM.dd -> LocalDate 로 변경
    public LocalDate getDealCanceledDate() {
        if (! StringUtils.hasText(dealCanceledDate)) {
            return null;
        }

        return LocalDate.parse(dealCanceledDate.trim(), DateTimeFormatter.ofPattern("yy.MM.dd"));
    }

    // 지번이 null (xml 에 node 가 없는경우) -> ""
    public String getJibun() {
        return Optional.ofNullable(jibun).orElse("");
    }

    // 소수점 2자리 이하 버림 -> DB의 전용면적이 소숫점 2자리
    public BigDecimal getExclusiveArea() {
        return exclusiveArea.setScale(2, RoundingMode.FLOOR);
    }

    // 거래 년월일 -> LocalDate 로 변경
    public LocalDate getDealDate() {
        return LocalDate.of(year, month, day);
    }
}
