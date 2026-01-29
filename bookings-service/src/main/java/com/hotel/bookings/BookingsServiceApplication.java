package com.hotel.bookings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.hotel"})
@EnableJpaAuditing
@EnableFeignClients
public class BookingsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingsServiceApplication.class, args);
    }

}
