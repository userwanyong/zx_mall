package com.xixi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 */
@Data
public class Shop {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private String category;
    private String img;
    private BigDecimal price;
    private String status;
    private Integer userId;
    private String saleStatus;
    private Integer readCount;

    private String time;

}
