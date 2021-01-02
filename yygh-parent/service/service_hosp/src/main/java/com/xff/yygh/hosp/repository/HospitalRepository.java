package com.xff.yygh.hosp.repository;

import com.xff.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HospitalRepository  extends MongoRepository<Hospital,String> {
           // 医院的  操作mongo 数据库的接口对象
           //名字符合规范 (注意名字)，它就会自动创建方法，不需要自己实现
   // 判断数据是否已经在数据库存在了
    Hospital getHospitalByHoscode(String hoscode);

    //客户端的查询 根据医院名称获取医院列表
    List<Hospital> findHospitalByHosnameLike(String hosname);

}
