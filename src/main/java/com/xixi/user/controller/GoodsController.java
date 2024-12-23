package com.xixi.user.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.AuthAccess;
import com.xixi.user.common.Result;
import com.xixi.user.entity.Goods;
import com.xixi.user.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    //增
    @PostMapping("/add")
    public Result add(@RequestBody Goods Goods) {
        Goods.setTime(DateUtil.now());
        goodsService.save(Goods);
        return Result.success();
    }

    //改(根据id修改)
    @PutMapping("/update")
    public Result update(@RequestBody Goods Goods) {
        goodsService.updateById(Goods);
        return Result.success();
    }

    //删(单个删除)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        goodsService.removeById(id);
        return Result.success();
    }

    //删除(批量删除)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        goodsService.removeBatchByIds(ids);
        return Result.success();
    }

    //信息(全部查询)
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Goods> Goodslist = goodsService.list(new QueryWrapper<Goods>().orderByDesc("id"));
        return Result.success(Goodslist);
    }


    //信息(单个查询，根据用户id)
    @AuthAccess
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Goods Goods = goodsService.getById(id);
        return Result.success(Goods);
    }

    //信息(分页模糊查询)
    //管理员接口
    @AuthAccess//用于忽略token限制
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        Page<Goods> page = goodsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }


    //信息(分页模糊查询)
    //用户接口
    @AuthAccess//用于忽略token限制
    @GetMapping("/selectByPageUser")
    public Result selectByPageUser(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.eq("open", 1);//用户只能看到对外出售的商品
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        Page<Goods> page = goodsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

}
