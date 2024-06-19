package com.example.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Assignment;
import com.example.mapper.AssignmentMapper;
import com.example.service.AssignmentService;
import org.springframework.stereotype.Service;

@Service
public class AssignServiceImpl extends ServiceImpl<AssignmentMapper,Assignment> implements AssignmentService {
}
