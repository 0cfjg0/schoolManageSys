package com.example.exception;

import com.example.entity.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.transform.Result;

@RestControllerAdvice//相当于@ResponseBody+@ControllerAdvice
public class GlobalExceptionHandler {//全局异常处理器
    @ExceptionHandler(Exception.class)//接收需要处理的异常的字节码文件
    public R<String> exceptionHandler(Exception exception){
        //打印异常信息
        exception.printStackTrace();
        return R.error(exception.getMessage());
    }
}