package com.psc.sample.r104;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// --job.name=jpaPageJob v=1 chunkSize=10
@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class R104Application {

    public static void main(String[] args) {

        SpringApplication.run(R104Application.class, args);
    }

}
