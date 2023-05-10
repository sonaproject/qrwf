package com.goblin.qrwf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QrwfApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrwfApplication.class, args);
    }

}
