package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.entity.Lawd;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fastcampus.housebatch.core.entity.QLawd.lawd;

@Repository
public class LawdQueryRepository extends QuerydslRepositorySupport {
    public LawdQueryRepository() {
        super(Lawd.class);
    }

    public List<String> findDistinctGuLawdCd() {
        return from(lawd)
                .select(lawd.lawdCd.substring(0, 5))
                .where(lawd.lawdCd.like("%00000"),
                        lawd.lawdCd.notLike("%00000000"),
                        lawd.exist.eq(true))
                .fetch();
    }
}
