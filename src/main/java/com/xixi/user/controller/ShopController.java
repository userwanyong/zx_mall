package com.xixi.user.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.HoneyLogs;
import com.xixi.user.common.LogType;
import com.xixi.user.common.Result;
import com.xixi.user.entity.Shop;
import com.xixi.user.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/shop")
public class ShopController {


    @Autowired
    ShopService shopService;

    //增
    @HoneyLogs(operation = "商品", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Shop Shop) {
        shopService.save(Shop);
        return Result.success();
    }

    //改(根据id修改)
    @HoneyLogs(operation = "商品", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody Shop Shop) {
        Shop.setTime(DateUtil.now());
        shopService.updateById(Shop);
        return Result.success();
    }

    //删(单个删除)
    @HoneyLogs(operation = "商品", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        shopService.removeById(id);
        return Result.success();
    }

    //删除(批量删除)
    @HoneyLogs(operation = "商品", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        shopService.removeBatchByIds(ids);
        return Result.success();
    }

    //信息(全部查询)
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Shop> Shoplist = shopService.list(new QueryWrapper<Shop>().orderByDesc("id"));
        return Result.success(Shoplist);
    }


    //信息(单个查询，根据用户id)
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Shop Shop = shopService.getById(id);
        return Result.success(Shop);
    }


    //信息(分页模糊查询)
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name,
                               @RequestParam String category) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<Shop>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(category), "category", category);
        Page<Shop> page = shopService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }


}
