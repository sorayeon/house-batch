package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.core.repository.LawdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;


/**
 * ExcutionContext 에 저장할 데이터
 * 1. guLawdCd - 구 코드 -> 다음 스텝에서 활용할 값
 * 2. guLawdCdList - 구 코드 리스트
 * 3. itemCount - 남아있는 구 코드 갯수
 */
@RequiredArgsConstructor
public class GuLawdTasklet implements Tasklet {

    private static final String KEY_ITEM_COUNT = "itemCount";
    private static final String KEY_GU_LAWD_CD_LIST = "guLawdCdList";
    private static final String KEY_GU_LAWD_CD = "guLawdCd";
    private final LawdRepository lawdRepository;
    private List<String> guLawdCdList;
    private int itemCount;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        ExecutionContext executionContext = getExecutionContext(chunkContext);

        initList(executionContext);
        initItemCount(executionContext);

        // 데이터가 있으면 다음 스텝을 실행하도록 하고, 데이터가 없으면 종료되도록 한다.
        // 데이터가 있으면 => CONTINUABLE
        if (itemCount == 0) {
            contribution.setExitStatus(ExitStatus.COMPLETED);
            return RepeatStatus.FINISHED;
        }

        itemCount--;
        executionContext.putString(KEY_GU_LAWD_CD, guLawdCdList.get(itemCount));
        executionContext.putInt(KEY_ITEM_COUNT, itemCount);

        contribution.setExitStatus(new ExitStatus("CONTINUABLE"));
        return RepeatStatus.FINISHED;
    }

    private void initList(ExecutionContext executionContext) {
        if (executionContext.containsKey(KEY_GU_LAWD_CD_LIST)) {
            guLawdCdList = (List<String>) executionContext.get(KEY_GU_LAWD_CD_LIST);
        } else {
            guLawdCdList = lawdRepository.findDistinctGuLawdCd();
            executionContext.put(KEY_GU_LAWD_CD_LIST, guLawdCdList);
            executionContext.putInt(KEY_ITEM_COUNT, guLawdCdList.size());
        }
    }

    private void initItemCount(ExecutionContext executionContext) {
        if (executionContext.containsKey(KEY_ITEM_COUNT)) {
            itemCount = executionContext.getInt(KEY_ITEM_COUNT);
        } else {
            itemCount = guLawdCdList.size();
        }
    }

    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        // ExecutionContext 사용시 주의1) 읽기전용 Map 으로 만들어지며 (수정불가)
        // chunkContext.getStepContext().getJobExecutionContext().;
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        return stepExecution.getJobExecution().getExecutionContext();
    }


}
