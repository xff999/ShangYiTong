package com.xff.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.xff.yygh") //整合的swagger 在另个模块，要引入，要扫描公共的包部分
@EnableDiscoveryClient  //开启注册发现服务
@EnableFeignClients(basePackages = "com.xff")  //在这个开启远程调用 的注解
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
        }
    }

