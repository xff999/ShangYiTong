package com.xff.yygh.hosp.repository;

import com.xff.yygh.model.hosp.Department;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository  //注解        // 科室的  操作mongo 数据库的接口对象
public interface DepartmentRepository  extends MongoRepository<Department,String> {


        //根据医院编号和科室编号  查询到科室
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
