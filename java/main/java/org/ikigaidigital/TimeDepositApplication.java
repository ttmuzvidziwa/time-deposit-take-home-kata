package org.ikigaidigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimeDepositApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TimeDepositApplication.class, args);
    }
}
