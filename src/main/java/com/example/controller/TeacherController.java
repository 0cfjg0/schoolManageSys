package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.Assignment;
import com.example.entity.Course;
import com.example.entity.R;
import com.example.entity.User;
import com.example.exception.TestException;
import com.example.service.CourseService;
import com.example.service.UserService;
import com.example.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private CourseService courseService;

    @Resource
    private UserService userService;

    @Resource
    private HttpServletRequest request;

    /**
     *发布课程
     * @param course
     * @return
     */
    @PostMapping("/publish")
    public R<Void> publishCourse(@RequestBody Course course){
        Long id = TokenUtil.getCurrentId(request);
        //校验登录人
        if(userService.getById(id).getRole()!= User.Role.TEACHER){
            throw new TestException("登录人不为教师");
        }

        //校验课程是否重复
        LambdaQueryWrapper<Course> wrapper = Wrappers.<Course>lambdaQuery();
        wrapper.eq(Course::getName,course.getName()).or().eq(Course::getId,course.getId());
        if(ObjectUtil.isNotEmpty(courseService.getOne(wrapper))){
            throw new TestException("已存在同名课程");
        }

        //发布课程
        try {
            courseService.save(course);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestException("添加失败");
        }
        return R.success("添加成功");
    }

    /**
     * 修改课程
     */
    @PutMapping("/update")
    public R<Void> updateCourse(@RequestBody Course course){
        Long id = TokenUtil.getCurrentId(request);
        //校验登录人
        if(userService.getById(id).getRole()!= User.Role.TEACHER){
            throw new TestException("登录人不为教师");
        }

        //校验课程是否存在
        LambdaQueryWrapper<Course> queryWrapper = Wrappers.<Course>lambdaQuery();
        queryWrapper.eq(Course::getName,course.getName()).or().eq(Course::getId,course.getId());
        if(ObjectUtil.isEmpty(courseService.getOne(queryWrapper))){
            throw new TestException("不存在此课程");
        }

        //修改课程
        LambdaUpdateWrapper<Course> updateWrapper = Wrappers.<Course>lambdaUpdate();
        updateWrapper.eq(Course::getName,course.getName()).or().eq(Course::getId,course.getId());
        try {
            courseService.update(course,updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestException("修改失败");
        }
        return R.success("添加成功");
    }

    /**
     * 删除课程
     * @param courseId
     * @return
     */
    @DeleteMapping("/delete/{courseId}")
    public R<Void> deleteCourse(@PathVariable Long courseId){
        Long id = TokenUtil.getCurrentId(request);
        //校验登录人
        if(userService.getById(id).getRole()!= User.Role.TEACHER){
            throw new TestException("登录人不为教师");
        }

        //校验课程是否存在
        Course course = courseService.getById(courseId);
        if(ObjectUtil.isEmpty(course)){
            throw new TestException("不存在此课程");
        }

        //删除课程
        try {
            courseService.removeById(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestException("删除失败");
        }
        return R.success("删除成功");
    }

    @PostMapping("/assign")
    public R<Void> assignHomeWork(@RequestBody Assignment assignment){
        Long id = TokenUtil.getCurrentId(request);
        //校验登录人
        if(userService.getById(id).getRole()!= User.Role.TEACHER){
            throw new TestException("登录人不为教师");
        }

        //查询对应作业
        LambdaQueryWrapper<Assignment> queryWrapper = Wrappers.<Assignment>lambdaQuery();

//        //删除课程
//        try {
//            courseService.removeById(courseId);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new TestException("删除失败");
//        }
//        return R.success("删除成功");
    }
}
