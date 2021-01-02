package com.xff.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xff.cmn.client.DictFeignClient;
import com.xff.yygh.hosp.repository.HospitalRepository;
import com.xff.yygh.hosp.service.HospitalService;
import com.xff.yygh.model.hosp.Hospital;
import com.xff.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sun.security.ssl.Debug;

import javax.annotation.Resource;
import java.util.*;


@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired   //注入操作mongo 数据库的对象
    private HospitalRepository  hospitalRepository;
    @Resource
    private DictFeignClient dictFeignClient;  //feign远程调用的对象

    //上传医院接口
    @Override
    public void save(Map<String, Object> paramMap) {
          //把参数map 集合 转换成对象 Hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString,Hospital.class);//string转换为对象

        //判断数据是否已经在数据库存在了
        String   hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

         //如果存在，做修改操作
        if(hospitalExist!=null){
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }else{  //做添加
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }
    //根据医院编号查询
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }


    //医院的 分页查询
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
              //0为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        //查询条件的对象转换为 医院的对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

              //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

                //创建查询实例
        Example<Hospital> example = Example.of(hospital, matcher);

        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
                   //得到医院信息的集合
                   // List<Hospital> content = pages.getContent();
        //直接使用 流的方式遍历 查出的 list 集合，进行附加信息的封装
        pages.getContent().stream().forEach(item ->{
            this.setHospitalHosType(item);
        });

        return pages;
    }


    //查出的 list 集合，进行附加信息的封装
    private Hospital setHospitalHosType(Hospital hospital) {
                 //调用远程 封装的方法   获得医院的等级
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        //查询 省，市，地区
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        //放入 存放附加信息的 map 集合中 ，对象中封装的
        hospital.getParam().put("fullAddress", provinceString+cityString+districtString);
        hospital.getParam().put("hostypeString",hostypeString );
        return  hospital;
    }

    //医院的更新 上线状态
    @Override
    public void updateStatus(String id, Integer status) {
         //根据传入的 id 得到这个医院信息的对象
        Hospital hospital = hospitalRepository.findById(id).get();
        //修改这个对象的状态信息
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        //把修改的对象保存进入 数据库
        hospitalRepository.save(hospital);

    }

    //根据id 查看医院的详情
    @Override
    public Map<String,Object> getHospById(String id) {

        Map<String, Object> result = new HashMap<>();
                  //查到医院，调用上面的方法，把附加信息 上传到这个对象中
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
       // System.out.println(id+"得到的id");

        result.put("hospital", hospital);  //医院信息，
         //d单独 的可以查询其他的信息               //信息分开处理，更美观  符合逻辑
        result.put("bookingRule", hospital.getBookingRule()); //预定信息
        hospital.setBookingRule(null);//不需要重复返回
        return result;
    }

    //获取医院名称
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(null != hospital) {
            return hospital.getHosname();
        }
        return "";
    }
    //客户端的查询 根据医院名称获取医院列表
    @Override
    public List<Hospital> findByHosname(String hosname) {

        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

   // 客户端的查询 hoscode 医院预约挂号详情
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }


}
