package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.dto.UserDto;
import com.example.entity.R;
import com.example.entity.User;
import com.example.exception.TestException;
import com.example.service.UserService;
import com.example.utils.JWTUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisTemplate;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {
    @Resource
    UserService userService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 登录
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody UserDto userDto){
        User user = BeanUtil.toBean(userDto,User.class);
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        wrapper.eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword());
        User res = userService.getOne(wrapper);
        //用户不存在
        if(ObjectUtil.isEmpty(res)){
            throw new TestException("用户名或密码错误");
        }else{
            //存入用户id
            Map map = MapUtil.builder().put("id",res.getId()).build();

            //用户信息存入Redis
            String jsonStr = JSONUtil.toJsonStr(res);
            try {
                BoundValueOperations<String, String> opts = stringRedisTemplate.boundValueOps(res.getId().toString());
                opts.set(jsonStr,2*60, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
                throw new TestException("Redis缓存失败");
            }

            //返回secret
            return R.success(JWTUtils.generateJwt(map),"登录成功");
        }
    }

    /**
     * 注册
     * @param userDto
     * @return
     */
    @PostMapping("/regist")
    public R<Void> regist(@RequestBody UserDto userDto){
        User user = BeanUtil.toBean(userDto, User.class);
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
