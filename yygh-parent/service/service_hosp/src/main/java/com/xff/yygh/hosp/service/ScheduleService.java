package com.xff.yygh.hosp.service;

import com.xff.yygh.model.hosp.Schedule;
import com.xff.yygh.vo.hosp.ScheduleOrderVo;
import com.xff.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    //上传  值班信息的接口
    void save(Map<String, Object> paramMap);

    // 查询 值班信息的接口
    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    //删除排班信息 接口方法
    void remove(String hoscode, String hosScheduleId);

    //根据医院编号 和科室信息 ，查询排版规则数据
    Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode);

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    //获取可预约排班数据
    Map<String ,Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);
    //获取排班id获取排班数据
    Schedule getScheduleId(String scheduleId);

    //根据排班id获取预约下单数据
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    // 修改排班,更新排班数据 用于mp
    void update(Schedule schedule);




}
