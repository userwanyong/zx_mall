package com.xixi.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xixi.user.entity.Goods;
import org.apache.ibatis.annotations.Update;

/**
 * @author 永
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Update("update goods set store=#{store} where id=#{id}")
    void saveNewStore(Goods goods);


}
