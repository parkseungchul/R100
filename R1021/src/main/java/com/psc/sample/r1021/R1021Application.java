package com.psc.sample.r1021;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class R1021Application {

    public static void main(String[] args) {
        SpringApplication.run(R1021Application.class, args);
    }

}
