package com.psc.sample.r1021.batch;

import com.psc.sample.r1021.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class FixedLengthJob2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int chunkSize = 5;

    @Bean
    public Job fixedLengthJob2_batchBuild() throws Exception {
        return jobBuilderFactory.get("fixedLengthJob2")
                .start(fixedLengthJob2_batchStep1())
                .build();
    }

    @Bean
    public Step fixedLengthJob2_batchStep1() throws Exception {
        return stepBuilderFactory.get("fixedLengthJob2_batchStep1")
                .<TwoDto, TwoDto>chunk(chunkSize)
                .reader(fixedLengthJob2_FileReader())
                .writer(fixedLengthJob2_FileWriter(new FileSystemResource("R102/output/fixedLengthJob2_output.txt")))
                .build();
    }

    @Bean
    public FlatFileItemReader<TwoDto> fixedLengthJob2_FileReader() {

        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/fixedLengthJob2_input.txt"));
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

    @Bean
    public FlatFileItemWriter<TwoDto> fixedLengthJob2_FileWriter(Resource outputResource) throws Exception {
        BeanWrapperFieldExtractor<TwoDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"one", "two"});
        fieldExtractor.afterPropertiesSet();

        FormatterLineAggregator<TwoDto> lineAggregator = new FormatterLineAggregator<>();

        // 왼쪽, 오른쪽
        lineAggregator.setFormat("%-5s###%5s");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<TwoDto>()
                .name("fixedLengthJob2_FileWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }
}
