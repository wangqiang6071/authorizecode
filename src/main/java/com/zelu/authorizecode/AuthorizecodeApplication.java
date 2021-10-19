package com.zelu.authorizecode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = {"com.zelu.authorizecode.dao"})
public class AuthorizecodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizecodeApplication.class, args);
    }
}
