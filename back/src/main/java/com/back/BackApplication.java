package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement//能够开启数据库事务
@EnableAsync
public class BackApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }
}
