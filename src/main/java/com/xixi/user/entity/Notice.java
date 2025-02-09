package com.xixi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 永
 */
@Data
public class Notice {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer userid;
    private String ctime;
    private String utime;
    private Boolean open;

    //这个注解表示这个字段不在数据库表里  是用来做业务处理的
    @TableField(exist = false)
    private String user;

}
