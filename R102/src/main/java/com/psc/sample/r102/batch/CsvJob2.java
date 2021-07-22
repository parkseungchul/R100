package com.psc.sample.r102.batch;

import com.psc.sample.r102.custom.CustomBeanWrapperFieldExtractor;
import com.psc.sample.r102.dto.TwoDto;
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
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class CsvJob2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int chunkSize = 5;

    @Bean
    public Job csvJob1csvJob2_batchBuild() throws Exception {
        return jobBuilderFactory.get("csvJob2")
                .start(csvJob2_batchStep1())
                .build();
    }

    @Bean
    public Step csvJob2_batchStep1() throws Exception {
        return stepBuilderFactory.get("csvJob2_batchStep1")
                .<TwoDto, TwoDto>chunk(chunkSize)
                .reader(csvJob2_FileReader())
                .writer(csvJob2_FileWriter(new FileSystemResource("R102/output/csvJob2_output.txt")))
                .build();
    }

    @Bean
    public FlatFileItemReader<TwoDto> csvJob2_FileReader() {

        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/csvJob2_input.csv"));
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<TwoDto> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("one","two");
        delimitedLineTokenizer.setDelimiter(":");

        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter<TwoDto> csvJob2_FileWriter(Resource outputResource) throws Exception {

        // 방법의 일환일 뿐 processor 권장 함
        CustomBeanWrapperFieldExtractor<TwoDto> fieldExtractor = new CustomBeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"one", "two"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<TwoDto> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("-");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<TwoDto>()
                .name("csvJob2_FileWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }
}
