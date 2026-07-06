package com.jobtracker;

import com.jobtracker.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class JobTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobTrackerApplication.class, args);
    }
}
