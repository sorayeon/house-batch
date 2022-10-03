package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.entity.AptDeal;

import java.time.LocalDate;
import java.util.List;

public interface AptDealQueryRepository {
    List<AptDeal> findByDealCanceledIsFalseAndDealDateEquals(LocalDate dealDate);
}
