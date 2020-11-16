package com.lpf.ecs_springboot.common;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理器，aop  @RestControllerAdvice
 *
 */
@RestControllerAdvice
public class RRExceptionHandler {


    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e){
        return R.error("已存在该记录");
    }




    //统一异常信息，响应ajax
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        return R.error("错误信息："+e.getMessage());
    }



}
