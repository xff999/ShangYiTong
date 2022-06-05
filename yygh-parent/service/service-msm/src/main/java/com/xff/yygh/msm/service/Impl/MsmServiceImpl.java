package com.xff.yygh.msm.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.netflix.client.ClientException;
import com.xff.yygh.common.result.Result;
import com.xff.yygh.msm.service.MsmService;
import com.xff.yygh.vo.msm.MsmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
public class MsmServiceImpl implements MsmService {


    //发送手机验证码
    @Override
    public boolean send(String phone, String code) {
       //判断手机号是否为空
        if (StringUtils.isEmpty(phone)) {
            return false;
        }

        // 整合容联云短信服务
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8aaf0708809721d0018109ebe0af2778"; // 需修改
        String accountToken = "a3bf9f1aade14f499394fdeb9ec914f2"; // 需修改
        //请使用管理控制台中已创建应用的APPID
        String appId = "8aaf0708809721d0018109ebe189277f";  // 需修改
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);

        String to = phone;
        // String to = "15993216986";

        String templateId = "1";
        String code1 = code;

        String[] datas = {code1, "3"};

        HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            return true;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
          return false;
        }


  }
  //发送预约信息的
    private boolean send(String phone, Map<String ,Object> param) {
        //判断手机号是否为空
        if (StringUtils.isEmpty(phone)) {
            return false;
        }

        // 整合容联云短信服务
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8aaf0708809721d0018109ebe0af2778"; // 需修改
        String accountToken = "a3bf9f1aade14f499394fdeb9ec914f2"; // 需修改
        //请使用管理控制台中已创建应用的APPID
        String appId = "8aaf0708809721d0018109ebe189277f";  // 需修改
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);

        String to = phone;
        // String to = "15993216986";

        String templateId = "1";


        String[] datas = {String.valueOf(param), "3"};

        HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            return true;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            return false;
        }


    }

    //发送 预约短信
    @Override
    public boolean send(MsmVo msmVo) {
        if(!StringUtils.isEmpty(msmVo.getPhone())) {
            return this.send(msmVo.getPhone(), msmVo.getParam());
        }
        return false;
    }


}

