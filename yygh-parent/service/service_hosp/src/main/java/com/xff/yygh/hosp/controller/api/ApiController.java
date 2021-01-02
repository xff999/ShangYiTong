package com.xff.yygh.hosp.controller.api;


import com.xff.yygh.hosp.service.ScheduleService;
import com.xff.yygh.model.hosp.Schedule;
import com.xff.yygh.vo.hosp.ScheduleOrderVo;
import com.xff.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import com.xff.yygh.common.exception.YyghException;
import com.xff.yygh.common.helper.HttpRequestHelper;
import com.xff.yygh.common.result.Result;
import com.xff.yygh.common.result.ResultCodeEnum;
import com.xff.yygh.common.utils.MD5;
import com.xff.yygh.hosp.service.DepartmentService;
import com.xff.yygh.hosp.service.HospitalService;
import com.xff.yygh.hosp.service.HospitalSetService;
import com.xff.yygh.model.hosp.Department;
import com.xff.yygh.model.hosp.Hospital;
import com.xff.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
//@CrossOrigin  //跨域访问要加入的注解  -->使用gateway 的配置类，就不用加了
public class ApiController {
     //关于调用api 的接口 控制层，  这里调用 医院模拟模块的服务，
             //  实现对 医院信息，科室，排班  的修改
    @Autowired
    private HospitalService hospitalService;  //操作 上传医院信息的 service

    @Autowired
    private DepartmentService departmentService; //操作 上传科室的信息 service

    @Autowired
    private ScheduleService scheduleService; //操作 上传排班信息的 service

    @Autowired
    private HospitalSetService hospitalSetService; // 调用 hospset 里面的方法


    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
          //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

         //验证签名
         //1.取出传过的 签名 (已加密md5)，和数据库比对验证
          String hospSign = (String) paramMap.get("sign");
         //2.得到参数里面的 医院编号，根据编号查询医院
          String hoscode =(String ) paramMap.get("hoscode");
        //3.根据 编号 查询数据库里面的 医院对应的签名
         String signKey = hospitalSetService.getSignKey(hoscode);
         //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
         if (! hospSign.equals(signKeyMD5)) {
             throw new YyghException(ResultCodeEnum.SIGN_ERROR);
         }

          //处理 图标传输的 过程出现的问题 “+”  ===》转换成了 “”  所以要换回来
        String logoData =(String) paramMap.get("logoData");
         logoData = logoData.replaceAll(" ", "+");
          //放回集合传输中
         paramMap.put("logoData", logoData);

        //调用service 方法
        hospitalService.save(paramMap);
        return Result.ok();

    }
    //查询医院接口
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //2.获取传入的医院编号
        String hoscode =(String ) paramMap.get("hoscode");

        //验证签名  可以抽取为工具类
        //1.取出传过的 签名 (已加密md5)，和数据库比对验证
        String hospSign = (String) paramMap.get("sign");

        //3.根据 编号 查询数据库里面的 医院对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
        if (! hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service方法实现 根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);

        return Result.ok(hospital);
    }


    //上传科室接口方法
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //拿到传入的数据
        //获取传递过来的科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //验证签名  可以抽取为工具类
        //1.取出传过的 签名 (已加密md5)，和数据库比对验证
        String hospSign = (String) paramMap.get("sign");

        //2.获取传入的医院编号
        String hoscode =(String ) paramMap.get("hoscode");
        //3.根据 编号 查询数据库里面的 医院对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
        if (! hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(paramMap); //调用 科室service 传入 上传的参数，实现添加数据库
        return  Result.ok();
    }
    //查询科室接口方法
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        //拿到传入的数据
        //获取传递过来的科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //验证签名  可以抽取为工具类
        //1.取出传过的 签名 (已加密md5)，和数据库比对验证
        String hospSign = (String) paramMap.get("sign");

        //2.获取传入的医院编号
        String hoscode =(String ) paramMap.get("hoscode");
        //3.根据 编号 查询数据库里面的 医院对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
        if (! hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //获得 当前页，和每页记录数
        //  Integer page = (String) paramMap.get("page");
         //判断当前页是否等于空
        int page = StringUtils.isEmpty(paramMap.get("page")
                                  ) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")
                                  ) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        //创建分页查询的条件对象  Model 里面封装了对象
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
          //设置查询条件
          departmentQueryVo.setHoscode(hoscode);  //查询这个医院里面的 科室
        Page<Department> pageModel =departmentService.findPageDepartment(page,limit,departmentQueryVo);


        return Result.ok(pageModel);
    }
    //删除科室接口方法
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //拿到传入的数据
        //获取传递过来的科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //验证签名  可以抽取为工具类
        //1.取出传过的 签名 (已加密md5)，和数据库比对验证
        String hospSign = (String) paramMap.get("sign");

        //2.获取传入的医院编号
        String hoscode =(String ) paramMap.get("hoscode");
        //3.根据 编号 查询数据库里面的 医院对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
        if (! hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //获得科室编号
        String depcode = (String) paramMap.get("depcode");

        departmentService.remove(hoscode,depcode);

        return  Result.ok();
    }



    //上传  值班信息的接口
    @PostMapping("saveSchedule")
    public Result  saveSchedule(HttpServletRequest request){
        //拿到传入的数据
        //获取传递过来的 排版信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //验证签名  可以抽取为工具类
        //1.取出传过的 签名 (已加密md5)，和数据库比对验证
        String hospSign = (String) paramMap.get("sign");

        //2.获取传入的医院编号
        String hoscode =(String ) paramMap.get("hoscode");
        //3.根据 编号 查询数据库里面的 医院对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
        if (! hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service的方法，上传信息
           scheduleService.save(paramMap); //传入路径的参数信息 map
         return Result.ok();


    }
    // 查询 值班信息的接口
    @PostMapping("schedule/list")
    public Result  findSchedule(HttpServletRequest request){
        //拿到传入的数据
        //获取传递过来的  排班信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //验证签名  可以抽取为工具类
        //1.取出传过的 签名 (已加密md5)，和数据库比对验证
        String hospSign = (String) paramMap.get("sign");

        //2.获取传入的医院编号
        String hoscode =(String ) paramMap.get("hoscode");
        //3.根据 编号 查询数据库里面的 医院对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
        if (! hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //科室的编号
        String depcode =(String ) paramMap.get("depcode");

        //获得 当前页，和每页记录数
        //  Integer page = (String) paramMap.get("page");
        //判断当前页是否等于空
        int page = StringUtils.isEmpty(paramMap.get("page")
        ) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")
        ) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        //创建分页查询的条件对象  Model 里面封装了对象
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        //设置查询条件
        scheduleQueryVo.setHoscode(hoscode);  //查询这个医院, 这个科室，里面的排班信息
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel =scheduleService.findPageSchedule(page,limit,scheduleQueryVo);


        return Result.ok(pageModel);
    }
    //删除排班信息 接口方法
    @PostMapping("schedule/remove")
    public Result remove(HttpServletRequest request){
        //拿到传入的数据
        //获取传递过来的  排班信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //转换数据格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //验证签名  可以抽取为工具类
        //1.取出传过的 签名 (已加密md5)，和数据库比对验证
        String hospSign = (String) paramMap.get("sign");

        //2.获取传入的医院编号
        String hoscode =(String ) paramMap.get("hoscode");
        //3.根据 编号 查询数据库里面的 医院对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //4.查询后，进行加密判断
        String signKeyMD5 = MD5.encrypt(signKey);
        if (! hospSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //获得科室编号
        String depcode = (String) paramMap.get("depcode");
        //获取排班编号
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        scheduleService.remove(hoscode,hosScheduleId);  //根据医院和排班编号

        return  Result.ok();
    }


}
