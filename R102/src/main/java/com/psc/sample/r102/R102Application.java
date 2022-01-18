package com.psc.sample.r102;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;

@EnableBatchProcessing
@SpringBootApplication
public class R102Application {

    public static void main(String[] args) {
        SpringApplication.run(R102Application.class, args);
    }

}
