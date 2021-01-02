package com.xff.yygh.hosp.controller;


import com.xff.yygh.common.result.Result;
import com.xff.yygh.hosp.service.HospitalService;
import com.xff.yygh.model.hosp.Hospital;
import com.xff.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin  //跨域访问要加入的注解  -->使用gateway 的配置类，就不用加了
@Api(tags = "医院基础信息") //配置swagger的中文提示
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    //医院的列表 分页查询
    @ApiOperation(value = "获取分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listHosp(
            //@ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            //@ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
           // @ApiParam(name = "hospitalQueryVo", value = "查询对象", required = false)
                    HospitalQueryVo hospitalQueryVo) {
        return Result.ok(hospitalService.selectHospPage(page, limit, hospitalQueryVo));
    }

    //医院的更新 上线状态
    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("/updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id,
                                   @PathVariable Integer status){

        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //根据id 查看医院的详情
    @ApiOperation(value = "获取医院详情")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id) {
        Map<String,Object> map = hospitalService.getHospById(id);
        return Result.ok(map);  //注意对象的返回
    }


}




