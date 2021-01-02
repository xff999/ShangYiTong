package com.xff.yygh.hosp.service;

import com.xff.yygh.model.hosp.Hospital;
import com.xff.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {

    //上传医院接口
    void save(Map<String, Object> paramMap);
    //根据医院编号查询
    Hospital getByHoscode(String hoscode);

    // 医院的 分页查询
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    //医院的更新 上线状态
    void updateStatus(String id, Integer status);

    //根据id 查看医院的详情
    Map<String,Object> getHospById(String id);

    //获取医院名称
    String getHospName(String hoscode);

    //客户端的查询 根据医院名称获取医院列表
    List<Hospital> findByHosname(String hosname);

    //客户端的查询 医院预约挂号详情
    Map<String, Object> item(String hoscode);
}
