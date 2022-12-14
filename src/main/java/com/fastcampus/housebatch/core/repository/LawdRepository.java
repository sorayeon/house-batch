package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.entity.Lawd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LawdRepository extends JpaRepository<Lawd, Integer>, LawdQueryRepository {

    Optional<Lawd> findByLawdCd(String lawdCd);

}
