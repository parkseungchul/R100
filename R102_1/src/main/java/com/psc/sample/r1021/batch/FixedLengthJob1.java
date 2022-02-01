package com.psc.sample.r1021.batch;

import com.psc.sample.r1021.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class FixedLengthJob1 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int chunkSize = 5;

    @Bean
    public Job fixedLengthJob1_batchBuild() {
        return jobBuilderFactory.get("fixedLengthJob1")
                .start(fixedLengthJob1_batchStep1())
                .build();
    }

    @Bean
    public Step fixedLengthJob1_batchStep1() {
        return stepBuilderFactory.get("fixedLengthJob1_batchStep1")
                .<TwoDto, TwoDto>chunk(chunkSize)
                .reader(fixedLengthJob1_FileReader())
                .writer(twoDto -> twoDto.stream().forEach(twoDto2 -> {
                    log.debug(twoDto2.toString());
                }))
                .build();
    }

    @Bean
    public FlatFileItemReader<TwoDto> fixedLengthJob1_FileReader() {

        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/fixedLengthJob1_input.txt"));
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<TwoDto> defaultLineMapper = new DefaultLineMapper<>();


        FixedLengthTokenizer fixedLengthTokenizer = new FixedLengthTokenizer();

        fixedLengthTokenizer.setNames("one", "two");
        fixedLengthTokenizer.setColumns(new Range(1,5), new Range(6,10));

        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class);

        defaultLineMapper.setLineTokenizer(fixedLengthTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }
}
