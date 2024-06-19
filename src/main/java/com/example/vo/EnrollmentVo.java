package com.example.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("enrollment")
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentVo {
    //学生id
    private Long studentId;

    //课程id
    private Long courseId;
}
