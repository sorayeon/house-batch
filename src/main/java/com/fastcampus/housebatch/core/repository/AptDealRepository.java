package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.entity.Apt;
import com.fastcampus.housebatch.core.entity.AptDeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface AptDealRepository extends JpaRepository<AptDeal, Long> {
    Optional<AptDeal> findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(Apt apt, BigDecimal exclusiveArea, LocalDate dealDate, Long dealAmount, Integer floor);
}
