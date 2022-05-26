package com.xff.yygh.hosp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xff.yygh.hosp.mapper.HospitalSetMapper;
import com.xff.yygh.hosp.service.HospitalSetService;
import com.xff.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//创建service接口的实现类
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet>
                                    implements HospitalSetService {
                      //继承mp 封装的 impl 传入参数，，实现自己的service接口
//    @Autowired
//    private HospitalSetMapper hospitalSetMapper;

    //根据 编号 查询数据库里面的 医院对应的签名
    @Override
    public String getSignKey(String hoscode) {
           //创建查询条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
          wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
             //可能会空指针
        return hospitalSet.getSignKey();
    }
}
