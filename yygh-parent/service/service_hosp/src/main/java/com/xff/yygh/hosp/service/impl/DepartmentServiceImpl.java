package com.xff.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xff.yygh.hosp.repository.DepartmentRepository;
import com.xff.yygh.hosp.service.DepartmentService;
import com.xff.yygh.model.hosp.Department;
import com.xff.yygh.vo.hosp.DepartmentQueryVo;
import com.xff.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service    //注入操作mongo 数据库的对象
public  class DepartmentServiceImpl implements DepartmentService {

     @Autowired
    private DepartmentRepository departmentRepository;

    //上传科室接口方法
    @Override
    public void save(Map<String, Object> paramMap) {
        //1. map 集合数据 转换为  department对象形式
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString, Department.class);

        //调用规范化的  方法名，查询mongo 数据库 中是否已经存在
       Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(
                                  department.getHoscode(),department.getDepcode());

        //判断数据库中是否含有这个数据
        if(departmentExist!=null){
              //修改对象属性的 基本值  更新信息
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);

            departmentRepository.save(departmentExist);  //存在就 更新这个数据
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department); //直接传入对象
        }

    }

    //查询科室接口方法
    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //创建 Pageable 对象，设置当前页和每页记录数  0 是第一页
        Pageable pageable = PageRequest.of(page-1,limit);

        //把查询的对象，转换为 部门的实体类形式
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        //创建 Example 对象，传入查询条件
           ExampleMatcher matcher = ExampleMatcher.matching()  //传入条件
                       .withIgnoreCase(true)
                       .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
          Example<Department> example = Example.of(department,matcher);

          Page<Department> all = departmentRepository.findAll(example,pageable);

         return all;
    }

    //删除科室接口方法
    @Override
    public void remove(String hoscode, String depcode) {
        //根据医院编号和 科室编号查询
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);

        //判断数据库是否有
        if (department!=null){
            //调用方法删除
            departmentRepository.deleteById(department.getId());
        }
    }
//========================================================

//根据医院编号，查询医院的所有科室信息
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        List<DepartmentVo> result =new ArrayList<>(); //存放 查询的结果集合
        //根据编号查到科室
            //创建查询条件的实体
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);  //传入对象的 编号
        Example example = Example.of(departmentQuery);  //封装查询
           //调用mongo 数据库的方法 得到所有的科室
        List<Department> departmentList = departmentRepository.findAll(example);

          //封装数据格式 为 json
        // 根据科室的 编号信息，分为不同的组，在获得下面子分组
        //使用流的方式 计算处理
       Map<String, List<Department>>  departmentMap =
                         departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
                                             //根据大科室编号分类
       //遍历结合大科室 的 map   得到内部子科室
        for (Map.Entry<String,List<Department>> entry: departmentMap.entrySet() ) {

            String bigcode = entry.getKey(); //大科室编号
            List<Department> departmentChildList =entry.getValue(); //大科室对应的 子科室

            //封装大科室
            DepartmentVo bigDepartVo = new DepartmentVo();  //创建大科室对象
              bigDepartVo.setDepcode(bigcode);
              bigDepartVo.setDepname(departmentChildList.get(0).getDepname());

            //封装小科室
            List<DepartmentVo> children = new ArrayList<>(); //创建小科室的 集合对象
                //遍历子科室里面的 集合，得到里面的小科室
             for(Department department: departmentChildList){
                 DepartmentVo departmentChild = new DepartmentVo(); //创建每个小科时的对象
                  //封装 进入数据
                 departmentChild.setDepcode(department.getDepcode());
                 departmentChild.setDepname(department.getDepname());
                   //加入小科室的集合
                 children.add(departmentChild);
             }
             //把小科室的list 集合放入，大科室的chrildren 属性中
            bigDepartVo.setChildren(children);

             // 封装的大科室信息  放入最终的 result 要返回的集合中
            result.add(bigDepartVo);
         }

        return result;
    }
    //根据医院编号科室编号，得到科室名称
    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null) {
            return department.getDepname();
        }
        return null;
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
    }


}
