package com.sy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sy.mapper")
public class SyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyApplication.class, args);
    }

}
