package com.xff.yygh.user.api;


import com.xff.yygh.common.result.Result;
import com.xff.yygh.common.utils.AuthContextHolder;
import com.xff.yygh.model.user.UserInfo;
import com.xff.yygh.user.service.UserInfoService;
import com.xff.yygh.vo.user.LoginVo;
import com.xff.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Api("用户登录的接口")
//@CrossOrigin
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

       //用户使用手机号登录
    @ApiOperation(value = "手机号登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    //用户认证接口
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        //传递两个参数，第一个参数用户id，第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    //获取用户id信息接口
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}
