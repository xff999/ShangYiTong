package com.xff.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xff.yygh.model.hosp.HospitalSet;
import com.xff.yygh.vo.order.SignInfoVo;


public interface HospitalSetService extends IService<HospitalSet> {
          // service 继承 mp 提供的基础iservice ,传入实体类操作对象

   // 根据 编号 查询数据库里面的 医院对应的签名
    String getSignKey(String hoscode);
//获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);
}
