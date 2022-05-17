package com.xff.yygh.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

//加入自动配置的 mapper 文件映射的 类bean
@Configuration
@MapperScan("com.xff.yygh.hosp.mapper")
public class HospConfig {
}