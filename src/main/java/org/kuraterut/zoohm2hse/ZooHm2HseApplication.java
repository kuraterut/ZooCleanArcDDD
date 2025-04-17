package org.kuraterut.zoohm2hse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZooHm2HseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZooHm2HseApplication.class, args);
    }
}
