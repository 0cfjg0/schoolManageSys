package com.example.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
public class CourseDto {
    private Long id;

    //课程名称
    private String name;

    //教师id
    private Long teacher_id;

    //开始日期
    private Date startDate;

    //结束日期
    private Date endDate;

    //创建时间
    Timestamp createdAt;
}
