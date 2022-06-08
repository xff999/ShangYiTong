package com.xff.cmn.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
        //使用远程调用的接口注解
public interface DictFeignClient {
     //下面添加方法 和路径

     @GetMapping(value = "/admin/cmn/dict/getName/{dictCode}/{value}")
     public String getName(
             @PathVariable("dictCode") String dictCode,
             @PathVariable("value") String value);

     @GetMapping(value = "/admin/cmn/dict/getName/{value}")
     public String getName(
             @PathVariable("value") String value);
}
