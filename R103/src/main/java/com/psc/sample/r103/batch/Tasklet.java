package com.psc.sample.r103.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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
public class Tasklet implements org.quartz.Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        tasklet_batchBuild();
    }


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job tasklet_batchBuild() {
        return jobBuilderFactory.get("taskletJob")
                .start(tasklet_step1()).next(tasklet_step2(null))
                .build();
    }

    @Bean
    public Step tasklet_step1() {
        return stepBuilderFactory.get("taskletStep1")
                .tasklet((a,b) -> {
                    log.debug("-> job -> [step1]");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step tasklet_step2(@Value("#{jobParameters[date]}") String date){
        return stepBuilderFactory.get("taskStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.debug("-> step1 -> [step2]:" + date);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
