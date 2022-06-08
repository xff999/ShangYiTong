package com.xff.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xff.yygh.model.user.UserInfo;
import com.xff.yygh.vo.user.LoginVo;
import com.xff.yygh.vo.user.UserAuthVo;
import com.xff.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    //用户使用手机号登录
    Map<String, Object> loginUser(LoginVo loginVo);

    //判断数据库是否存在微信的扫描人信息
    UserInfo selectWxInfoOpenId(String openid);
    //用户认证接口
    void userAuth(Long userId, UserAuthVo userAuthVo);

    //用户列表（条件查询带分页）
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);
    //用户锁定
    void lock(Long userId, Integer status);
    //用户详情
    Map<String,Object> show(Long userId);
    //认证审批
    void approval(Long userId, Integer authStatus);
}
