package com.example.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.example.service.CourseService;
import org.springframework.stereotype.Service;


@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
