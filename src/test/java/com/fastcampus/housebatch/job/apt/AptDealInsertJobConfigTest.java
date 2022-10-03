package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {AptDealInsertJobConfig.class, BatchTestConfig.class})
class AptDealInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private AptDealService aptDealService;

    @MockBean
    private LawdRepository lawdRepository;

    @MockBean
    private ApartmentApiResource apartmentApiResource;

    @Test
    public void success() throws Exception {
        // given
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("41135", "41136"));
        when(apartmentApiResource.getResource(anyString(), any())).thenReturn(
                new ClassPathResource("apartments-api-response-test.xml")
        );

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob(new JobParameters(Maps.newHashMap("yearMonth", new JobParameter("2021-07"))));

        // then
        assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        verify(aptDealService, times(4)).upsert(any());
    }

    @Test
    public void fail_whenYearMonthNotExist() throws Exception {
        // given
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("41135"));
        when(apartmentApiResource.getResource(anyString(), any())).thenReturn(
                new ClassPathResource("apartments-api-response-test.xml")
        );

        // when
        // then
        assertThrows(JobParametersInvalidException.class, () -> jobLauncherTestUtils.launchJob());
        verify(aptDealService, never()).upsert(any());
    }
}