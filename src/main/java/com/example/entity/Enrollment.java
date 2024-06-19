package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("enrollment")
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
    //学生id
    @MppMultiId
    @TableField("student_id")
    private Long studentId;

    //课程id
    @MppMultiId
    @TableField("course_id")
    private Long courseId;
}
