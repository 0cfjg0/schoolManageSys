package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * 学生查询已选课程
     *
     * @return
     */
    @GetMapping("")
    public R<List<CourseVo>> checkCourse() {
        Long id = TokenUtil.getCurrentId(request);
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
            return R.success(res);
        }
    }

    /**
     * 学生选课
     * @param course
     * @return
     */
    @PostMapping("/select")
    public R<Void> selectCourse(@RequestBody Course course) {
        Long id = TokenUtil.getCurrentId(request);

        //查询课程
        LambdaQueryWrapper<Course> wrapper = Wrappers.<Course>lambdaQuery();
        wrapper.eq(Course::getName,course.getName());
        Course co = courseService.getOne(wrapper);
        if(ObjectUtil.isEmpty(co)){
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
