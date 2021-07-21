package com.psc.sample.r102.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Tasklet {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job taskletJob() {
        return jobBuilderFactory.get("taskletJob")
                .start(taskStep1()).next(taskStep2(null))
                .build();
    }

    @Bean
    public Step taskStep1() {
        return stepBuilderFactory.get("taskletStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.debug("-> job -> [step1]");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step taskStep2(@Value("#{jobParameters[date]}") String date){
        return stepBuilderFactory.get("taskStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.debug("-> step1 -> [step2]:" + date);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
