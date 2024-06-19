package com.example.Interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis-plus自动填充策略设置
 */
@Slf4j
@Component
public class DatetimeMetaObjectHandler implements MetaObjectHandler {
    //进行插入时填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
    }
 
    //进行修改操作时填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
    }


}