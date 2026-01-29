package com.hotel.form;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hotel.form", "com.hotel.common"})
public class FormServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormServiceApplication.class, args);
    }
}
