package com.psc.sample.r102.batch;

import com.psc.sample.r102.custom.CustomPassThroughLineAggregator;
import com.psc.sample.r102.dto.OneDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

// Text -> Text
@RequiredArgsConstructor
@Slf4j
@Configuration
public class TextJob2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int chunkSize = 5;

    @Bean
    public Job textJob2_batchBuild() {
        return jobBuilderFactory.get("textJob2")
                .start(textJob2_batchStep1())
                .build();
    }

    @Bean
    public Step textJob2_batchStep1() {
        return stepBuilderFactory.get("textJob2_batchStep1")
                .<OneDto, OneDto>chunk(chunkSize)
                .reader(textJob2_FileReader())
                .writer(textJob2_FileWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<OneDto> textJob2_FileReader() {
        FlatFileItemReader<OneDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/textJob2_input.txt"));
        flatFileItemReader.setLineMapper((line, lineNumber) -> new OneDto(line));
        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter textJob2_FileWriter() {
        return  new FlatFileItemWriterBuilder<OneDto>()
                .name("textJob2_FileWriter")
                .resource(new FileSystemResource("R102/output/textJob2_output.txt"))
                .lineAggregator(new CustomPassThroughLineAggregator<>())
                .build();
    }
}
