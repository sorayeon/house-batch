package com.fastcampus.housebatch.job.notify;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.adapter.FakeSendService;
import com.fastcampus.housebatch.core.dto.AptDto;
import com.fastcampus.housebatch.core.entity.AptNotification;
import com.fastcampus.housebatch.core.entity.Lawd;
import com.fastcampus.housebatch.core.repository.AptNotificationRepository;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Slf4j
@ContextConfiguration(classes = {AptNotificationJobConfig.class, BatchTestConfig.class})
class AptNotificationJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AptNotificationRepository aptNotificationRepository;

    @MockBean
    private AptDealService aptDealService;

    @MockBean
    private LawdRepository lawdRepository;

    @MockBean
    private FakeSendService fakeSendService;

    @AfterEach
    public void tearDown() {
        aptNotificationRepository.deleteAll();
    }

    @Test
    public void success() throws Exception {
        // given
        LocalDate dealDate = LocalDate.now().minusDays(1);
        String guLawdCd = "11110";
        String email = "abc@gmail.com";
        String anotherEmail = "efg@gmail.com";

        givenAptNotification(guLawdCd, email, true);
        givenAptNotification(guLawdCd, anotherEmail, false);
        givenLawdCd(guLawdCd);
        givenAptDeal(guLawdCd, dealDate);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(
                new JobParameters(Maps.newHashMap("dealDate", new JobParameter(dealDate.toString())))
        );

        // then
        assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        verify(fakeSendService, times(1)).send(eq(email), anyString());
        verify(fakeSendService, never()).send(eq(anotherEmail), anyString());
    }


    private void givenAptNotification(String guLawdCd, String email, boolean enabled) {
        AptNotification notification = AptNotification.builder()
                .email(email)
                .guLawdCd(guLawdCd)
                .enabled(enabled)
                .build();
        aptNotificationRepository.save(notification);
    }

    private void givenLawdCd(String guLawdCd) {
        String lawdCd = guLawdCd + "00000";

        Lawd lawd = Lawd.builder()
                .lawdCd(lawdCd)
                .lawdDong("????????? ????????? ?????????")
                .exist(true)
                .build();
        when(lawdRepository.findByLawdCd(lawdCd))
                .thenReturn(Optional.of(lawd));
    }

    private void givenAptDeal(String guLawdCd, LocalDate dealDate) {
        when(aptDealService.findByGuLawdCdAndDealDate(guLawdCd, dealDate))
                .thenReturn(Arrays.asList(
                        new AptDto("IT?????????", 200000000L),
                        new AptDto("???????????????", 150000000L),
                        new AptDto("??????????????????", 230000000L)
                ));
    }
}