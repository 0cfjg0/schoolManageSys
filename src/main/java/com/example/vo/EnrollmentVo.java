package com.example.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("enrollment")
public class EnrollmentVo {
    //学生id
    private Long studentId;

    //课程id
    private Long courseId;
}
