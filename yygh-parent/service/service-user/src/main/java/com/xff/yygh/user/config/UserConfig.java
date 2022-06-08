package com.xff.yygh.user.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.xff.yygh.user.mapper") //扫描mapper映射文件的规则
public class UserConfig {

}
