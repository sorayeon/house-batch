package com.fastcampus.housebatch.core.service;

import com.fastcampus.housebatch.core.entity.Lawd;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LawdService {
    private final LawdRepository lawdRepository;

    @Transactional
    public void upsert(Lawd lawd) {
        // 데이터가 존재하면 수정, 없으면 생성
        Lawd saved = lawdRepository.findByLawdCd(lawd.getLawdCd())
                .orElseGet(() -> Lawd.builder()
                        .lawdCd(lawd.getLawdCd())
                        .lawdDong(lawd.getLawdDong())
                        .exist(lawd.getExist())
                        .build());

        if (saved.getLawdId() == null) {
            lawdRepository.save(saved);
        } else if(saved != lawd) {
            saved.updateLawd(lawd);
            lawdRepository.save(saved);
        }
    }
}
