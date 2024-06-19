package com.example.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@TableName("course")
@AllArgsConstructor
@NoArgsConstructor
public class CourseVo {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    //课程名称
    private String name;

    //教师id
    private Long teacherId;

    //开始日期
    private Date startDate;

    //结束日期
    private Date endDate;

    //创建时间
    Timestamp createdAt;
}
