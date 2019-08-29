package com.eim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.eim.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class Starbucks20190826Application {

    public static void main(String[] args) {
        SpringApplication.run(Starbucks20190826Application.class, args);
    }

}
