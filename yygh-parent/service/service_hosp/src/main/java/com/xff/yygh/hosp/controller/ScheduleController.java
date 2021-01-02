package com.xff.yygh.hosp.controller;


import com.xff.yygh.common.result.Result;
import com.xff.yygh.hosp.service.ScheduleService;
import com.xff.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin  //跨域访问要加入的注解  -->使用gateway 的配置类，就不用加了
@Api(tags = "医院排班信息") //配置swagger的中文提示
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //根据医院编号 和科室信息 ，查询排版规则数据
    @ApiOperation(value = "查询排版规则数据")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable Long page,
                           @PathVariable Long limit,
                           @PathVariable String hoscode,
                           @PathVariable String depcode){
        Map<String ,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
       return Result.ok(map);
    }

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail( @PathVariable String hoscode,
                                     @PathVariable String depcode,
                                     @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }

}
