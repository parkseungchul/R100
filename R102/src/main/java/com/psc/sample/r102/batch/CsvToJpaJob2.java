package com.psc.sample.r102.batch;

import com.psc.sample.r102.domain.Two;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.persistence.EntityManagerFactory;

/**
 * Multiple Csv -> Jpa
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class CsvToJpaJob2 {


    @Autowired
    ResourceLoader resourceLoader;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 5;

    @Bean
    public Job csvToJpaJob2_batchBuild() throws Exception {
        return jobBuilderFactory.get("csvToJpaJob2")
                .start(csvToJpaJob2_batchStep1())
                .build();
    }

    @Bean
    public Step csvToJpaJob2_batchStep1() throws Exception {
        return stepBuilderFactory.get("csvToJpaJob2_batchStep1")
                .<Two, Two>chunk(chunkSize)
                .reader(csvToJpaJob2_FileReader())
                .processor(csvToJpaJob2_processor())
                .writer(csvToJpaJob2_dbItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Two, Two> csvToJpaJob2_processor() {
        return two -> new Two(two.getOne(), two.getTwo());
    }

    @Bean
    public JpaItemWriter<Two> csvToJpaJob2_dbItemWriter() {
        JpaItemWriter<Two> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }


    @Bean
    @SneakyThrows
    public MultiResourceItemReader<Two> csvToJpaJob2_FileReader() {

        MultiResourceItemReader<Two> resourceItemReader = new MultiResourceItemReader<Two>();
        resourceItemReader.setResources(ResourcePatternUtils
                .getResourcePatternResolver(this.resourceLoader).getResources("classpath:sample/csvToJpaJob2/*.txt"));
        resourceItemReader.setDelegate(multiFileItemReader());
        return resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<Two> multiFileItemReader() {
        FlatFileItemReader<Two> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setLineMapper((line, lineNumber) -> {
            String[] lines = line.split(":");
            return new Two(lines[0], lines[1]);
        });
        return flatFileItemReader;
    }
}
