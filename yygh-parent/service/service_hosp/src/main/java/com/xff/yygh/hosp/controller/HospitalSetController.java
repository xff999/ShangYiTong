package com.xff.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xff.yygh.common.result.Result;
import com.xff.yygh.common.utils.MD5;
import com.xff.yygh.hosp.service.HospitalSetService;
import com.xff.yygh.model.hosp.Hospital;
import com.xff.yygh.model.hosp.HospitalSet;
import com.xff.yygh.vo.hosp.HospitalQueryVo;
import com.xff.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理") //配置swagger的中文提示
@RestController //合成的注解
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //1.测试查询医院的信息
    @ApiOperation(value = "获取所有医院设置") // api 开头 配置swagger的中文提示
    @GetMapping("findAll")  //localhost:8201/admin/hosp/hospitalSet/findAll
    public Result findAllHospitalSet(){
        //调用mapper方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);      //封装为自定义的数据格式      //封装为数据的json 格式
    }
    //2.测试逻辑删除
    @ApiOperation(value = "逻辑删除医院设置")  // api 开头 配置swagger的中文提示
    @DeleteMapping("{id}")  //localhost:8201/swagger-ui.html
    public Result removeHospSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if(flag){
            return Result.ok();
        } else {
            return  Result.fail();
        }
    }

    //3.条件查询带分页操作 (模糊名字，或者 id)
    @PostMapping("findPageHospSet/{current}/{limit}")  //注意了
    @ApiOperation(value = "分页查询医院设置")  // api 开头 配置swagger的中文提示
   public Result findPageHospSet(@PathVariable Long current,
                                 @PathVariable Long limit, //传入路径参数
                                 //传入查询的条件，封装为对象了 （添加注解，表示不是必须的）对应改为post 请求
                                 @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){ //
        //创建分页的对象 ，传入当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);

        //创建查询的条件语句对象
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        //先从对象里面得到信息
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        //判断这个条件是否为空，不为空，才添加到，条件对象中
        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname", hosname); //wapper 的条件查询
        }
        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode", hoscode);
        }
      //service 调用方法,实现分页 条件 ，传入page wrapper条件
       Page<HospitalSet> pagehospitalSet = hospitalSetService.page(page,wrapper);

        //返回结果
        return Result.ok(pagehospitalSet);

    }
    //4.添加医院设置
    @PostMapping("saveHospitalSet")
    @ApiOperation(value = "添加医院设置")  // api 开头 配置swagger的中文提示
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        // 注意 给 这个对象里面的参数 设置
        hospitalSet.setStatus(1); //设置状态 1 能使用， 0不能使用
        //签名密钥，自动生成的
        Random random = new Random();
             //在调用md5（工具类） 自动加密，
        String key = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(key); //对象设置 key

        //保存 对象， 其他参数外部传入
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //5.根据id 获取医院设置
    @GetMapping("getHospitalSet/{id}")
    @ApiOperation(value = "id查询医院设置")
    public Result getHospitalSet(@PathVariable Long id){
        // 根据id 查询出这个医院
        HospitalSet hospitalSet = hospitalSetService.getById(id);

        return  Result.ok(hospitalSet);

    }

    //6.修改医院设置
    @PostMapping("updateHospitalSet")
    @ApiOperation(value = "修改医院设置")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
         //路径上传入了修改的对象，直接调用修改方法
        boolean flag = hospitalSetService.updateById(hospitalSet);//这个方法自动寻找对象的id 底层mp 封装
        if(flag){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //7.批量删除医院设置
    @DeleteMapping("batchRemove")
    @ApiOperation(value = "id 批量删除医院设置")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean remove = hospitalSetService.removeByIds(idList);
        if(remove){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //8.医院设置 锁定和解锁的 （操作statue）
    @PutMapping("lockHospitalSet/{id}/{status}")
    @ApiOperation(value = "医院设置的状态，statue")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //9.发送生成的密钥，与医院方便对接
    @PutMapping("sendKey/{id}")
    @ApiOperation(value = "发送医院设置的密钥，对接")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }

}
