package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("assignment")
public class Assignment {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    //课程id
    private Long courseId;

    //学生id
    private Long studentId;

    //作业内容
    private String content;

    //成绩
    private Double grade;

    //创建时间
    private Timestamp created_at;
}
