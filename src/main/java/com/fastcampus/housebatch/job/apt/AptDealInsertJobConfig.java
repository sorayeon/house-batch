package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.job.validator.YearMonthParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AptDealInsertJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApartmentApiResource apartmentApiResource;
    private final LawdRepository lawdRepository;

    @Bean
    public Job aptDealInsertJob(
            Step guLawdCdStep,
            Step contextPrintStep
//            Step aptDealInsertStep
    ) {
        return jobBuilderFactory.get("aptDealInsertJob")
                .incrementer(new RunIdIncrementer())
                .validator(aptDealJobParameterValidator())
                .start(guLawdCdStep)
                .on("CONTINUABLE")
                .to(contextPrintStep)
                .next(guLawdCdStep)
                .from(guLawdCdStep)
                .on("*")
                .end()
                .end()
                .build();
    }

    private JobParametersValidator aptDealJobParameterValidator() {
        // --spring.batch.job.names=aptDealInsertJob -yearMonth=2022-07
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(
                new YearMonthParameterValidator()
        ));
        return validator;
    }

    @JobScope
    @Bean
    public Step guLawdCdStep(Tasklet guLawdCdTasklet) {
        return stepBuilderFactory.get("guLawdCdStep")
                .tasklet(guLawdCdTasklet)
                .build();
    }

    /**
     * ExcutionContext 에 저장할 데이터
     * 1. guLawdCd - 구 코드 -> 다음 스텝에서 활용할 값
     * 2. guLawdCdList - 구 코드 리스트
     * 3. itemCount - 남아있는 구 코드 갯수
     */
    @StepScope
    @Bean
    public Tasklet guLawdCdTasklet() {
        return (contribution, chunkContext) -> {
            StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
            ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

            // 데이터가 있으면 다음 스텝을 실행하도록 하고, 데이터가 없으면 종료되도록 한다.
            // 데이터가 있으면 => CONTINUABLE
            List<String> guLawdCdList;
            if (!executionContext.containsKey("guLawdCdList")) {
                guLawdCdList = lawdRepository.findDistinctGuLawdCd();
                executionContext.put("guLawdCdList", guLawdCdList);
                executionContext.putInt("itemCount", guLawdCdList.size());
            } else {
                guLawdCdList = (List<String>) executionContext.get("guLawdCdList");
            }

            int itemCount = executionContext.getInt("itemCount");

            if (itemCount == 0) {
                contribution.setExitStatus(ExitStatus.COMPLETED);
                return RepeatStatus.FINISHED;
            }

            itemCount--;
            String guLawdCd = guLawdCdList.get(itemCount);

            executionContext.putString("guLawdCd", guLawdCd);
            executionContext.putInt("itemCount", itemCount);

            contribution.setExitStatus(new ExitStatus("CONTINUABLE"));
            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    public Step contextPrintStep(
            Tasklet contextPrintTasklet
    ) {
        return stepBuilderFactory.get("contextPrintStep")
                .tasklet(contextPrintTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet contextPrintTasklet(@Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd) {
        return (contribution, chunkContext) -> {
            log.info("[contextPrintStep] guLawdCd={}", guLawdCd);
            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    public Step aptDealInsertStep(
            StaxEventItemReader<AptDealDto> aptDealResourceReader,
            ItemWriter<AptDealDto> aptDealWriter
    ) {
        return stepBuilderFactory.get("aptDealInsertStep")
                .<AptDealDto, AptDealDto>chunk(10)
                .reader(aptDealResourceReader)
                .writer(aptDealWriter)
                .build();
    }

    @StepScope
    @Bean
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
            @Value("#{jobParameters['yearMonth']}") String yearMonth,
            @Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd,
            Jaxb2Marshaller jaxb2Marshaller
    ) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
                .name("aptDealResourceReader")
                .resource(apartmentApiResource.getResource(guLawdCd, YearMonth.parse(yearMonth)))
                .addFragmentRootElements("item")
                .unmarshaller(jaxb2Marshaller)
                .build();
    }

    @StepScope
    @Bean
    public Jaxb2Marshaller aptDealDtoMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(AptDealDto.class);
        return jaxb2Marshaller;
    }

    @StepScope
    @Bean
    public ItemWriter<AptDealDto> aptDealWriter() {
        return items -> {
            items.forEach(System.out::println);
            System.out.println("============ Writing Completed ============="); // chunkSize 만큼씩 처리
        };
    }
}
