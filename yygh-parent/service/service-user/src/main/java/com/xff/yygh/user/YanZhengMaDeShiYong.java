package com.xff.yygh.user;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * @author zxb
 */
public class YanZhengMaDeShiYong {

    /**
     * 生成六位验证码
     *
     * @return 验证码
     */
    public static String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
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
        // 多个号码之间用英文逗号隔开，号码数不超过 200 个
        String to = "18392073976";
       // String to = "15993216986";
        // 模板id，官网控制台模板列表获取。测试模板写死是 1
        String templateId = "1";
        String code = generateCode();
        // datas 用于替换短信模板中的值，可以定义多个。这里替换模板中的 验证码 和 有效时间
        String[] datas = {code, "3"};
        // subAppend 扩展码，可选，四位数字 0~9999，具体用处尚不明确，建议注释
        // String subAppend = "1234";
        // reqId 第三方自定义消息id，可选，最大支持32位英文数字，同账号下同一自然天内不允许重复，建议注释
        // String reqId = "fadfafas";
        // HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas, subAppend, reqId);
        HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }
    }

}
