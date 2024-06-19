package com.example.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Enrollment;
import com.example.mapper.EnrollmentMapper;
import com.example.service.EnrollmentService;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {
}
