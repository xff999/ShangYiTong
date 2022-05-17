package com.xff.yygh.common.exception;

import com.xff.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理类
@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常处理类
    //只要发生异常，没有自定义就找到全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody   //返回结果json
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法,使用时要手动抛出 new
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e){
        return Result.build(e.getCode(), e.getMessage());
    }
}