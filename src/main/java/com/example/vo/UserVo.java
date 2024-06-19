package com.example.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class UserVo {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    //用户名
    private String username;

    //密码
    private String password;

    //用户角色
    private Role role;
    public enum Role{
        STUDENT,TEACHER,ADMIN;
    }

    //创建时间
    Timestamp createdAt;
}
