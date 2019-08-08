package com.example.batchpoc;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
@EnableBatchProcessing
public class BatchPocApplication {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        SpringApplication.run(BatchPocApplication.class, args);
    }

    @Bean
    JdbcTemplate template(DataSource source) {
        return new JdbcTemplate(source);
    }

    @Bean
    Job job() {
        return this.jobBuilderFactory.get("helloJob").start(step1()).build();
    }

    @Bean
    Tasklet helloTasklet() {
        return ((contribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext().getJobParameters().get("name");
            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    Step step1() {
        return this.stepBuilderFactory.get("step1").tasklet(helloTasklet()).build();
    }
}
