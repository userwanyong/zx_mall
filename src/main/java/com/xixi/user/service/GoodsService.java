package com.xixi.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixi.user.entity.Goods;
import com.xixi.user.mapper.GoodsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 永
 */
@Service
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {
    @Resource
    GoodsMapper GoodsMapper;

    public void saveNewStore(Goods goods) {
        GoodsMapper.saveNewStore(goods);
    }


}
