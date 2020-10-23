package com.zsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@SpringBootApplication
@EnableTransactionManagement
public class ZspdiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZspdiskApplication.class, args);
    }


}
