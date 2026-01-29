package com.hotel.guests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.hotel"})
@EnableJpaAuditing
@EnableFeignClients
public class GuestsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuestsServiceApplication.class, args);
    }

}
