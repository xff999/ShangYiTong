package com.xff.yygh.hosp.controller;


import com.xff.yygh.common.result.Result;
import com.xff.yygh.hosp.service.DepartmentService;
import com.xff.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin  //跨域访问要加入的注解  -->使用gateway 的配置类，就不用加了
@Api(tags = "医院的部门信息") //配置swagger的中文提示
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //根据医院编号，查询医院的所有科室信息
    @ApiOperation(value = "根据医院编号，查询医院的所有科室信息")
    @GetMapping("/getDepartmentList/{hoscode}")
    public Result getDepartmentList(@PathVariable String hoscode){
                //DepartmentVo 泛型使用这个实体类，它包括下层的 树结构孩子
        List<DepartmentVo> list  = departmentService.findDeptTree(hoscode);
         return Result.ok(list);

    }

}
