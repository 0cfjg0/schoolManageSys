package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Enrollment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnrollmentMapper extends BaseMapper<Enrollment> {
}
