package com.example.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@TableName("assignment")
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentVo {
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
