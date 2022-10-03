package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.entity.AptDeal;
import com.fastcampus.housebatch.core.entity.QApt;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.fastcampus.housebatch.core.entity.QAptDeal.aptDeal;

@Transactional(readOnly = true)
public class AptDealQueryRepositoryImpl extends QuerydslRepositorySupport implements AptDealQueryRepository {
    public AptDealQueryRepositoryImpl() {
        super(AptDeal.class);
    }

    @Override
    public List<AptDeal> findByDealCanceledIsFalseAndDealDateEquals(LocalDate dealDate) {
        // @Query("select ad from AptDeal ad join fetch ad.apt where ad.dealCanceled = 0 and ad.dealDate = ?1")
        return from(aptDeal)
                .join(aptDeal.apt, QApt.apt)
                .fetchJoin()
                .where(aptDeal.dealCanceled.eq(false),
                        aptDeal.dealDate.eq(dealDate))
                .fetch();
    }
}
