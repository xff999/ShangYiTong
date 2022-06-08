package com.xff.yygh.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xff.yygh.common.exception.YyghException;
import com.xff.yygh.common.help.JwtHelper;
import com.xff.yygh.common.result.Result;
import com.xff.yygh.common.result.ResultCodeEnum;
import com.xff.yygh.enums.AuthStatusEnum;
import com.xff.yygh.model.user.Patient;
import com.xff.yygh.model.user.UserInfo;
import com.xff.yygh.user.mapper.UserInfoMapper;
import com.xff.yygh.user.service.PatientService;
import com.xff.yygh.user.service.UserInfoService;
import com.xff.yygh.vo.user.LoginVo;
import com.xff.yygh.vo.user.UserAuthVo;
import com.xff.yygh.vo.user.UserInfoQueryVo;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
                                 implements UserInfoService
                                {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
     private PatientService patientService ;

    //用户使用手机号登录
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        //1.从 loginVo 获取输入的 信息 手机号，和验证码
        String phone = loginVo.getPhone();

        String userCode = loginVo.getCode();
        //2.判断手机号，和验证码 信息是否为空

        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(userCode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //3.判断手机收到的验证码和输入的是否一致
            //TODO 校验校验验证码
        //  生成验证码放到redis里面，设置有效时间
        String redisCode = redisTemplate.opsForValue().get(phone);


       if(!userCode.equals(redisCode)){  //用户的code 和短信 存在redis 中的不相同
            throw new YyghException(ResultCodeEnum.CODE_ERROR );
        }
     //绑定手机号码
        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if(null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }


        //4.判断 数据库 是否是第一次登录， （没有就注册）
            //使用手机号查询数据库
        //userInfo=null 说明手机直接登录
     if(null == userInfo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        //获取用户
         userInfo = baseMapper.selectOne(queryWrapper);
        if (null == userInfo) { //数据库中没有这个用户
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            baseMapper.insert(userInfo);
        }
    }
       //校验用户是否被禁用
        if(userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //TODO 记录登录

        //5.返回登录信息， token 信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        //TODO token 的生成 (使用jwt 工具类，在common_utils)
        String token = JwtHelper.createToken(userInfo.getId(), name);

        map.put("token", token);
        return map;
    }
      //判断数据库是否存在微信的扫描人信息
    @Override
   public UserInfo selectWxInfoOpenId(String openid) {
        return baseMapper.selectOne(new QueryWrapper<UserInfo>()
                  .eq("openid", openid));
      }

   //用户认证
     @Override
     public void userAuth(Long userId, UserAuthVo userAuthVo) {
        //根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
          //设置认证信息
           //认证人姓名
          userInfo.setName(userAuthVo.getName());
          //其他认证信息
          userInfo.setCertificatesType(userAuthVo.getCertificatesType());
          userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
          userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
          userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
          //进行信息更新
         baseMapper.updateById(userInfo);
    }


    //用户列表（条件查询带分页）
    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        //UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间
        //对条件值进行非空判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(status)) {
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(authStatus)) {
            wrapper.eq("auth_status",authStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        //调用mapper的方法
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return pages;
    }


    //编号变成对应值封装
   private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
         userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
         //处理用户状态 0  1
         String statusString = userInfo.getStatus().intValue()==0 ?"锁定" : "正常";
         userInfo.getParam().put("statusString",statusString);
         return userInfo;
     }

    //用户锁定
   @Override
   public void lock(Long userId, Integer status) {
        if(status.intValue()==0 || status.intValue()==1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

  //用户详情
     @Override
     public Map<String, Object> show(Long userId) {
        Map<String,Object> map = new HashMap<>();
           //根据userid查询用户信息
         UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
         map.put("userInfo",userInfo);
             //根据userid查询就诊人信息
         List<Patient> patientList = patientService.findAllUserId(userId);
         map.put("patientList",patientList);
         return map;
    }

 //认证审批  2通过  -1不通过
 @Override
 public void approval(Long userId, Integer authStatus) {
        if(authStatus.intValue()==2 || authStatus.intValue()==-1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }


   }
