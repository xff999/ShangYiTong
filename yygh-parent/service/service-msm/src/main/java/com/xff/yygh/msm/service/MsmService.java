package com.xff.yygh.msm.service;

import com.xff.yygh.vo.msm.MsmVo;

public interface MsmService {

    //发送手机验证码
    boolean send(String phone, String code);
//发送 预约短信
    boolean send(MsmVo msmVo);

}
