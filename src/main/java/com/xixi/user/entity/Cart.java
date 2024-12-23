package com.xixi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 */
@Data
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer goodsId;
    private Integer userId;
    private Integer num;
    private String time;

    @TableField(exist = false)
    private String goodsName;
    @TableField(exist = false)
    private String goodsImg;
    @TableField(exist = false)
    private BigDecimal price;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String name;
}
