package com.ecommerce.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.ecommerce.project",
        "com.ecommerce.exception"
})
public class SbEcomApplication {
    public static void main(String[] args) {
        SpringApplication.run(SbEcomApplication.class, args);
    }
}
