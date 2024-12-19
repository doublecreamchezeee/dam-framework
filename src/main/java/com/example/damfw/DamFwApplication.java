package com.example.damfw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class DamFwApplication {

    public static void main(String[] args) {
        SpringApplication.run(DamFwApplication.class, args);
    }

}
