package com.xixi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 永
 */
@Data
public class OrdersGoods {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Integer goodsId;
    private Integer num;
}