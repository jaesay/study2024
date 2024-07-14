package com.food.ordering.system.springbootk8sexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringBootK8sExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootK8sExampleApplication.class, args);
    }

}
