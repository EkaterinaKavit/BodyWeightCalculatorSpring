package com.example.BodyWeightCalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BodyWeightCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BodyWeightCalculatorApplication.class, args);
    }

}
