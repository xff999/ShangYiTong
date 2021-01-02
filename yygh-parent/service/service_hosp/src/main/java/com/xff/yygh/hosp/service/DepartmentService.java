package com.xff.yygh.hosp.service;


import com.xff.yygh.model.hosp.Department;
import com.xff.yygh.vo.hosp.DepartmentQueryVo;
import com.xff.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {

    //上传科室接口方法
    void save(Map<String, Object> paramMap);

    //查询科室接口方法
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    //删除科室接口方法
    void remove(String hoscode, String depcode);

//========================================
    //根据医院编号，查询医院的所有科室信息
    List<DepartmentVo> findDeptTree(String hoscode);

    //根据医院编号科室编号，得到科室名称
    String getDepName(String hoscode, String depcode);
    
    //根据医院编号科室编号，得到科对象
    Department getDepartment(String hoscode, String depcode);
}
