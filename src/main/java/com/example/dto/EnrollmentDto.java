package com.example.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentDto {
    //学生id
    private Long studentId;

    //课程id
    private Long courseId;
}
