package com.fastcampus.housebatch.job.lawd;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.core.service.LawdService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {LawdInsertJobConfig.class, BatchTestConfig.class})
class LawdInsertJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @MockBean
    private LawdService lawdService;

    @Test
    public void success() throws Exception {
        // when
        JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("LAWD_CODE_TEST.txt")));
        JobExecution execution = jobLauncherTestUtils.launchJob(parameters);

        // then
        assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        verify(lawdService, times(7)).upsert(any());
    }

    @Test
    public void fail_whenFileNotFound() throws Exception {
        // when
        JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("NOT_EXIST_LAWD_CODE_TEST.txt")));

        // then
        assertThrows(JobParametersInvalidException.class, () -> jobLauncherTestUtils.launchJob(parameters));
        verify(lawdService, never()).upsert(any());
    }

}