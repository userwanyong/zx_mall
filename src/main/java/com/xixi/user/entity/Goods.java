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
public class Goods {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private String unit;
    private String store;
    private String img;
    private String time;
    private Boolean open;

    @TableField(exist = false)
    private Integer num;

}
