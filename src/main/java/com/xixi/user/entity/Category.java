package com.xixi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 永
 */
@Data
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String categoryName;
    private String createUser;
    private String time;
    private Boolean open;
}
