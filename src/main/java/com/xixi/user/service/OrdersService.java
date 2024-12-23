package com.xixi.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixi.user.entity.Orders;
import com.xixi.user.mapper.OrdersMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> {
    @Resource
    OrdersMapper ordersMapper;

    public Orders selectByOrderNo(String no) {
        return ordersMapper.selectByOrderNo(no);
    }


}
