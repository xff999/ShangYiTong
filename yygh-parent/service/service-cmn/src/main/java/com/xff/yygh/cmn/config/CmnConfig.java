package com.xff.yygh.cmn.config;



import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//加入自动配置的 mapper 文件映射的 类bean
@Configuration
@MapperScan("com.xff.yygh.cmn.mapper")
public class CmnConfig {

    //分页插件  ,之一要写bean  装配置到spring
   @Bean
    public PaginationInterceptor paginationInterceptor(){
        return  new PaginationInterceptor();
    }
}
