package com.xixi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 永
 */
@Data
public class Orders {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String no;
    private BigDecimal totalPrice;
    private String state;
    private String time;
    private String payTime;
    private Integer userId;
    private String payNo;
    private String returnTime;

    @TableField(exist = false)
    private List<Cart> carts;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String nickname;
}
