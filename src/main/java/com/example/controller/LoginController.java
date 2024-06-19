package com.example.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.R;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.JWTUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Resource
    UserService userService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        wrapper.eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword());
        User res = userService.getOne(wrapper);
        //用户不存在
        if(ObjectUtil.isEmpty(res)){
            return R.error("用户名或密码错误");
        }else{
            //存入用户id
            Map map = MapUtil.builder().put("id",res.getId()).build();
            //返回secret
            return R.success(JWTUtils.generateJwt(map));
        }
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/regist")
    public R<Void> regist(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        wrapper.eq(User::getUsername,user.getUsername());
        User res = userService.getOne(wrapper);
        //用户已存在
        if(ObjectUtil.isNotEmpty(res)){
            return R.error("用户已存在");
        }else{
            userService.save(user);
            return R.success();
        }
    }
}
