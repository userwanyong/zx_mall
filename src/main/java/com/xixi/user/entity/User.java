package com.xixi.user.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 永
 */
@TableName("user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    @Alias("id")
    private Integer id;
    @Alias("用户名")
    private String username;
    @Alias("密码")
    private String password;
    @Alias("姓名")
    private String name;
    @Alias("姓别")
    private String gender;
    @Alias("电话")
    private String phone;
    @Alias("邮箱")
    private String email;
    @Alias("地址")
    private String address;
    @Alias("头像")
    private String avatar;
    @Alias("角色")
    private String role;

    @Alias("")
    @TableField(exist = false)
    private String token;

    @Alias("")
    @TableField(exist = false)
    private String oldPassword;

    @Alias("")
    @TableField(exist = false)
    private String newPassword;

    @Alias("")
    @TableField(exist = false)
    private String confirmPassword;

}
