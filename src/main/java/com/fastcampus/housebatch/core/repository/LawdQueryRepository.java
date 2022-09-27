package com.fastcampus.housebatch.core.repository;

import java.util.List;

public interface LawdQueryRepository {
    List<String> findDistinctGuLawdCd();
}
