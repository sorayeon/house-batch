package com.fastcampus.housebatch.core.service;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.entity.Apt;
import com.fastcampus.housebatch.core.entity.AptDeal;
import com.fastcampus.housebatch.core.repository.AptDealRepository;
import com.fastcampus.housebatch.core.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AptDealDto 에 있는 값을 Apt, AptDeal 엔티티로 저장한다.
 */
@RequiredArgsConstructor
@Service
public class AptDealService {
    private final AptRepository aptRepository;
    private final AptDealRepository aptDealRepository;

    @Transactional
    public void upsert(AptDealDto dto) {
        Apt apt = getAptOrNew(dto);
        saveAptDeal(dto, apt);
    }

    private Apt getAptOrNew(AptDealDto dto) {
        Apt apt = aptRepository.findByAptNameAndJibun(dto.getAptName(), dto.getJibun())
                .orElseGet(() -> Apt.from(dto));
        return aptRepository.save(apt);
    }

    private void saveAptDeal(AptDealDto dto, Apt apt) {
        AptDeal aptDeal = aptDealRepository.findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
                apt, dto.getExclusiveArea(), dto.getDealDate(), dto.getDealAmount(), dto.getFloor()
        ).orElseGet(() -> AptDeal.of(dto, apt));
        aptDeal.setDealCanceled(dto.isDealCanceled(), dto.getDealCanceledDate());
        aptDealRepository.save(aptDeal);
    }

}
