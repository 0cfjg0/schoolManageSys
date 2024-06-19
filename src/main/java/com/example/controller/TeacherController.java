package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.dto.AssignmentDto;
import com.example.dto.CourseDto;
import com.example.entity.*;
import com.example.exception.TestException;
import com.example.service.AssignmentService;
import com.example.service.CourseService;
import com.example.service.EnrollmentService;
import com.example.service.UserService;
import com.example.utils.TokenUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private CourseService courseService;

    @Resource
    private UserService userService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private AssignmentService assignmentService;

    @Resource
    private EnrollmentService enrollmentService;

    /**
     *发布课程
     * @param courseDto
     * @return
     */
    @PostMapping("/publish")
    public R<Void> publishCourse(@RequestBody CourseDto courseDto){
        Course course = BeanUtil.toBean(courseDto,Course.class);

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
    public R<Void> updateCourse(@RequestBody CourseDto courseDto){
        Course course = BeanUtil.toBean(courseDto,Course.class);

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
    @Transactional
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
            //删除关联的学生选课信息
            LambdaQueryWrapper<Enrollment> deleteWrapper = Wrappers.<Enrollment>lambdaQuery();
            deleteWrapper.eq(Enrollment::getCourseId,courseId);
            enrollmentService.remove(deleteWrapper);
            //删除课程
            courseService.removeById(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestException("删除失败");
        }
        return R.success("删除成功");
    }

    /**
     * 批改作业
     * @param assignmentDto
     * @return
     */
    @PostMapping("/assign")
    public R<Void> assignHomeWork(@RequestBody AssignmentDto assignmentDto){
        Assignment assignment = BeanUtil.toBean(assignmentDto,Assignment.class);

        Long id = TokenUtil.getCurrentId(request);
        //校验登录人
        if(userService.getById(id).getRole()!= User.Role.TEACHER){
            throw new TestException("登录人不为教师");
        }

        //校验当前登录老师是否有权限批改作业
        LambdaQueryWrapper<Course> teacherWrapper = Wrappers.<Course>lambdaQuery();
//        System.out.println(id+"---------"+assignment.getCourseId());
        teacherWrapper.eq(Course::getTeacherId,id).eq(Course::getId,assignment.getCourseId());
        List<Course> list = courseService.list(teacherWrapper);
        if(ObjectUtil.isEmpty(list)){
            throw new TestException("当前老师没有权限批改作业");
        }

        //批改作业
        try {
            if (!assignmentService.save(assignment)) {
                throw new TestException("批改失败");
            }
            return R.success("批改成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestException("批改失败");
        }
    }
}
