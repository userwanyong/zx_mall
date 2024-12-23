package com.xixi.user.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.Result;
import com.xixi.user.entity.OrdersGoods;
import com.xixi.user.service.OrdersGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/ordersGoods")
public class OrdersGoodsController {

    @Autowired
    OrdersGoodsService ordersGoodsService;

    //增
    @PostMapping("/add")
    public Result add(@RequestBody OrdersGoods ordersGoods) {
        ordersGoodsService.save(ordersGoods);
        return Result.success();
    }

    //改(根据id修改)
    @PutMapping("/update")
    public Result update(@RequestBody OrdersGoods ordersGoods) {
        ordersGoodsService.updateById(ordersGoods);
        return Result.success();
    }

    //删(单个删除)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        ordersGoodsService.removeById(id);
        return Result.success();
    }

    //删除(批量删除)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        ordersGoodsService.removeBatchByIds(ids);
        return Result.success();
    }

    //信息(全部查询)
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<OrdersGoods> ordersGoods = ordersGoodsService.list(new QueryWrapper<OrdersGoods>().orderByDesc("id"));
        return Result.success(ordersGoods);
    }

    //信息(单个查询，根据用户id)
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        OrdersGoods ordersGoods = ordersGoodsService.getById(id);
        return Result.success(ordersGoods);
    }

    //信息(分页模糊查询)
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name) {
        QueryWrapper<OrdersGoods> queryWrapper = new QueryWrapper<OrdersGoods>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        Page<OrdersGoods> page = ordersGoodsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }
}