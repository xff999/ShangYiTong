package com.xff.yygh.hosp.repository;


import com.xff.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository                 ///    值班信息的  操作mongo 数据库的接口对象
public interface ScheduleRepository extends MongoRepository<Schedule,String> {

     Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    //根据医院编号 和科室信息 ，查询排版规则数据
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
    //方法遵守规范， 框架直接自己创建
}
