package com.xff.yygh.hosp.controller.api;


import com.xff.yygh.common.result.Result;
import com.xff.yygh.hosp.service.DepartmentService;
import com.xff.yygh.hosp.service.HospitalService;
import com.xff.yygh.hosp.service.HospitalSetService;
import com.xff.yygh.hosp.service.ScheduleService;
import com.xff.yygh.model.hosp.Hospital;
import com.xff.yygh.model.hosp.Schedule;
import com.xff.yygh.vo.hosp.HospitalQueryVo;
import com.xff.yygh.vo.hosp.ScheduleOrderVo;
import com.xff.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
//@CrossOrigin
public class HospApiController {
           //客户端系统的查询调用，controller
      @Autowired
       private HospitalService hospitalService;
      @Autowired
      private DepartmentService departmentService; //注入部门的操作对象
      @Autowired
     private ScheduleService scheduleService;
      @Autowired
      private HospitalSetService hospitalSetService;


 //客户端的查询， 医院列表的，（直接调用 里里面的方法，上次写过）
    @ApiOperation(value = "获取分页列表")
    @GetMapping("/findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                        @PathVariable Integer limit,
                         HospitalQueryVo hospitalQueryVo) {
        //显示上线的医院
        //hospitalQueryVo.setStatus(1);
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }
    //客户端的查询 根据医院名称获取医院列表
    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(@PathVariable String hosname) {
      List<Hospital> list =  hospitalService.findByHosname(hosname);
        return Result.ok(list);
    }

    //客户端的查询 获取科室列表
    @ApiOperation(value = "获取科室列表")
    @GetMapping("department/{hoscode}")
    public Result index( @PathVariable String hoscode) {
        return Result.ok(departmentService.findDeptTree(hoscode));
    }
    //客户端的查询 医院预约挂号详情
    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("findospDetail/{hoscode}")
    public Result item( @PathVariable String hoscode) {
        return Result.ok(hospitalService.item(hoscode));
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingSchedule(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @PathVariable String hoscode,
            @PathVariable String depcode,
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }
  //获取排班id获取排班数据
    @ApiOperation(value = "获取排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(@PathVariable String scheduleId) {
        Schedule schedule = scheduleService.getScheduleId(scheduleId);
        return Result.ok(schedule);
    }


    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    @ApiOperation(value = "获取医院签名信息")
    @GetMapping("inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getSignInfoVo(hoscode);
    }


}













