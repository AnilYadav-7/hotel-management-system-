package com.hotel.rooms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hotel.rooms", "com.hotel.common"})
public class RoomsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomsServiceApplication.class, args);
    }
}
