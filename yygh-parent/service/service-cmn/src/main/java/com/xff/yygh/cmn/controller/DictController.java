package com.xff.yygh.cmn.controller;



import com.baomidou.mybatisplus.extension.api.R;
import com.xff.yygh.cmn.service.DictService;

import com.xff.yygh.common.result.Result;
import com.xff.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "数据字典接口") //配置swagger的中文提示
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin  //跨域访问要加入的注解  -->使用gateway 的配置类，就不用加了
public class DictController {
    //使用 swagger 进行测试  http://localhost:8202/swagger-ui.html#/

    @Autowired   //自动装配对象
    private DictService dictService;

    //1.根据数据id 查询子数据列表
    @ApiOperation(value = "根据数据id 查询子数据列表") // api 开头 配置swagger的中文提示
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
         List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    //2.导出数据字典的列表 进入用户指定的文件
    @ApiOperation(value = "导出数据字典的列表") // api 开头 配置swagger的中文提示
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
                  //参数存储 用户传入的数据地址
        //service对象调用  service 的方法
        dictService.exportData(response);
     }

    //3.导入数据进入，数字字典中的列表
    @ApiOperation(value = "导入进数据字典的列表") // api 开头 配置swagger的中文提示
    @PostMapping("/importData")   //post 请求
    public Result importData(MultipartFile file){
         dictService.importData(file);
        return Result.ok();
    }

    //4.根据上级编码与值 获取数据字典名称
    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                        @PathVariable("value") String value) {
        return dictService.getDictName(dictCode, value);
    }

    @GetMapping(value = "/getName/{value}")
    public String getName(
            @PathVariable("value") String value) {
        return dictService.getDictName("",value);
    }

    //5.根据dictCode 获取下级节点
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(@PathVariable String dictCode){
       List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }


}
