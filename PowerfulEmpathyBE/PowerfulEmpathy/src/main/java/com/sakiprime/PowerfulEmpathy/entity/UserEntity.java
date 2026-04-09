package com.sakiprime.PowerfulEmpathy.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("userdata")
public class UserEntity implements Serializable {//个性化项默认值为未设置。
    @TableId
    private Integer id;

    private String avatarKey;
    private String username;
    private String password;
    private String email;
    private String phone;
    @TableField(value = "QQ")
    private String QQ;
    private String gender;
    private String major;
    //报名信息。
    private String selectGroup;
    private String resumeKey;
    private String projectKey;

}
