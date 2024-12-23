package com.xixi.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 永
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    Page<Orders> page(Page<Object> objectPage, @Param("name") String name, @Param("role") String role, @Param("userId") Integer userId);

    @Select("select * from orders where no=#{no}")
    Orders selectByOrderNo(String no);


}
