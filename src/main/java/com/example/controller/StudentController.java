package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.dto.CourseDto;
import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.R;
import com.example.entity.User;
import com.example.exception.TestException;
import com.example.service.CourseService;
import com.example.service.EnrollmentService;
import com.example.service.UserService;
import com.example.utils.TokenUtil;
import com.example.vo.CourseVo;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    CourseService courseService;

    @Resource
    EnrollmentService enrollmentservice;

    @Resource
    UserService userService;

    @Resource
    HttpServletRequest request;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 学生查询已选课程
     *
     * @return
     */
    @GetMapping("")
    public R<List<CourseVo>> checkCourse() {
        Long id = TokenUtil.getCurrentId(request);
        //检查缓存内数据
        BoundValueOperations<String, String> opts = stringRedisTemplate.boundValueOps("student_course_list" + id);
        String redisData = opts.get();
        //获取缓存中的数据
        List<CourseVo> redisList = JSONUtil.parseArray(redisData)
                .stream()
                .map(item->JSONUtil.toBean((JSONObject) item,CourseVo.class))
                .collect(Collectors.toList());
        //如果缓存内有数据
        if (ObjectUtil.isNotEmpty(redisList)) {
            return R.success(redisList,"查询成功");
        } else {
            User user = userService.getById(id);
            if (user.getRole() != User.Role.STUDENT) {
                return R.error("登录用户不为学生");
            } else {
                //根据学生id获取选取课程信息
                LambdaQueryWrapper<Enrollment> wrapper = Wrappers.<Enrollment>lambdaQuery();
                wrapper.eq(Enrollment::getStudentId, user.getId());
                List<Enrollment> list = enrollmentservice.list(wrapper);
                //课程id列表
                List<Long> temp = list.stream().map(Enrollment::getCourseId).collect(Collectors.toList());
                List<Course> r = courseService.listByIds(temp);
                //转换成Vo
                List<CourseVo> res = r.stream().map(item -> BeanUtil.toBean(item, CourseVo.class)).collect(Collectors.toList());

                //Redis中存入学生选课信息
                try {
                    String jsonStr = JSONUtil.toJsonStr(r);
                    opts.set(jsonStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new TestException("Redis缓存失败");
                }

                return R.success(res, "查询成功");
            }

        }
    }

    /**
     * 学生选课
     *
     * @param courseDto
     * @return
     */
    @PostMapping("/select")
    public R<Void> selectCourse(@RequestBody CourseDto courseDto) {
        Course course = BeanUtil.toBean(courseDto, Course.class);

        Long id = TokenUtil.getCurrentId(request);

        //查询课程
        LambdaQueryWrapper<Course> wrapper = Wrappers.<Course>lambdaQuery();
        wrapper.eq(Course::getName, course.getName());
        Course co = courseService.getOne(wrapper);
        if (ObjectUtil.isEmpty(co)) {
            throw new TestException("没有找到对应的课程");
        }

        //添加学生选课关系
        Enrollment enrollment = Enrollment.builder().studentId(id).courseId(co.getId()).build();
        try {
            enrollmentservice.save(enrollment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestException("选课失败");
        }

        return R.success("选课成功");
    }
}
