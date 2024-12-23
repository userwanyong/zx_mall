package com.xixi.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.entity.Cart;
import org.apache.ibatis.annotations.Param;

/**
 * @author 永
 */
public interface CartMapper extends BaseMapper<Cart> {

    Page<Cart> page(Page<Object> objectPage, @Param("goodsId") Integer goodsId, @Param("role") String role, @Param("userId") Integer userId);
}
