package com.example.job;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.exception.TestException;
import com.example.service.CourseService;
import com.example.service.EnrollmentService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务处理器
 */
@Component
public class CFJG_job {

    @Resource
    CourseService courseService;

    @Resource
    EnrollmentService enrollmentService;

    /**
     * 清除过期课程
     * @throws Exception
     */
    @XxlJob("clearCourse")
    @Transactional
    public void clearCourse(){
        LambdaQueryWrapper<Course> wrapper = Wrappers.<Course>lambdaQuery();
        wrapper.lt(Course::getEndDate, Convert.toDate(LocalDateTime.now()));
        //得到id列表
        List<Long> list = courseService.list(wrapper).stream().map(Course::getId).collect(Collectors.toList());
        //删除课程
        try {
            //删除关联的学生选课信息
            for (Long id : list) {
                //课程id一致
                LambdaQueryWrapper<Enrollment> deleteWrapper = Wrappers.<Enrollment>lambdaQuery();
                deleteWrapper.eq(Enrollment::getCourseId,id);
                enrollmentService.remove(deleteWrapper);
            }
            //删除课程
            courseService.removeBatchByIds(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TestException("删除失败"+LocalDateTime.now());
        }
    }

}
