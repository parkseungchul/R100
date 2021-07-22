package com.psc.sample.r102.batch;

import com.psc.sample.r102.domain.Dept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

// DB -> log
@RequiredArgsConstructor
@Slf4j
@Configuration
public class JpaPageJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job jpaPageJob1_batchBuild() {
        return jobBuilderFactory.get("JpaPageJob1")
                .start(jpaPageJob1_batchStep1())
                .build();
    }

    @Bean
    public Step jpaPageJob1_batchStep1() {
        return stepBuilderFactory.get("JpaPageJob1_Step")
                .<Dept, Dept>chunk(chunkSize)
                .reader(jpaPageJob1_dbItemReader())
                .writer(jpaPageJob1_printItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob1_dbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("JpaPageJob1_Reader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d FROM Dept d order by dept_no asc")
                .build();
    }

    private ItemWriter<Dept> jpaPageJob1_printItemWriter() {
        return list -> {
            for (Dept dept: list) {
                log.debug(dept.toString());
            }
        };
    }
}
