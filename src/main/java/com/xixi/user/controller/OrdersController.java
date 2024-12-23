package com.xixi.user.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.Result;
import com.xixi.user.entity.*;
import com.xixi.user.mapper.OrdersMapper;
import com.xixi.user.service.CartService;
import com.xixi.user.service.GoodsService;
import com.xixi.user.service.OrdersGoodsService;
import com.xixi.user.service.OrdersService;
import com.xixi.user.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    @Resource
    OrdersMapper ordersMapper;

    @Resource
    OrdersGoodsService ordersGoodsService;

    @Resource
    CartService cartService;

    @Resource
    GoodsService goodsService;


    //增
    //购物车-->订单（多个）
    @PostMapping("/add")
    public Result add(@RequestBody Orders Orders) {
        User currentUser = TokenUtils.getCurrentUser();//获取当前登录用户信息
        Orders.setUserId(currentUser.getId());
        Date date = new Date();
        Orders.setTime(DateUtil.now());
        Orders.setNo(DateUtil.format(date, "yyyyMMdd") + System.currentTimeMillis());
        //创建订单
        ordersService.save(Orders);
        List<Cart> carts = Orders.getCarts();
        for (Cart cart : carts) {
            OrdersGoods ordersGoods = new OrdersGoods();
            ordersGoods.setGoodsId(cart.getGoodsId());
            ordersGoods.setNum(cart.getNum());
            ordersGoods.setOrderId(Orders.getId());
            ordersGoodsService.save(ordersGoods);
            //删除购物车数据
            cartService.removeById(cart.getId());
            //更新goods中库存数量
            Goods goods = goodsService.getById(cart.getGoodsId());
            goods.setStore(String.valueOf(Integer.parseInt(goods.getStore()) - cart.getNum()));
            goodsService.saveNewStore(goods);
        }
        return Result.success();
    }

    //购物车-->订单（单个）
    @PostMapping("/add2")
    public Result add2(@RequestBody Cart cart) {
        Orders Orders = new Orders();
        User currentUser = TokenUtils.getCurrentUser();//获取当前登录用户信息
        Orders.setUserId(currentUser.getId());
        Orders.setName(cart.getGoodsName());
        Orders.setTotalPrice(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));
        Date date = new Date();
        Orders.setTime(DateUtil.now());
        Orders.setNo(DateUtil.format(date, "yyyyMMdd") + System.currentTimeMillis());
        //创建订单
        ordersService.save(Orders);
        OrdersGoods ordersGoods = new OrdersGoods();
        ordersGoods.setGoodsId(cart.getGoodsId());
        ordersGoods.setNum(cart.getNum());
        ordersGoods.setOrderId(Orders.getId());
        ordersGoodsService.save(ordersGoods);
        //删除购物车数据
        cartService.removeById(cart.getId());
        //更新goods中库存数量
        Goods goods = goodsService.getById(cart.getGoodsId());
        goods.setStore(String.valueOf(Integer.parseInt(goods.getStore()) - cart.getNum()));
        goodsService.saveNewStore(goods);
        return Result.success();
    }


    //改(根据id修改)
    @PutMapping("/update")
    public Result update(@RequestBody Orders Orders) {
        ordersService.updateById(Orders);
        return Result.success();
    }

    //删(单个删除)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//这个id是orders中的id，不是goods_id
        ordersService.removeById(id);
        //更新goods中库存数量
        QueryWrapper<OrdersGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        List<OrdersGoods> ordersGoodsList = ordersGoodsService.list(queryWrapper);
        for (OrdersGoods ordersGoods : ordersGoodsList) {
            Integer goodsId = ordersGoods.getGoodsId();
            Goods goods = goodsService.getById(goodsId);
            goods.setNum(ordersGoods.getNum());
            goods.setStore(String.valueOf(Integer.parseInt(goods.getStore()) + ordersGoods.getNum()));
            goodsService.saveNewStore(goods);
        }
        return Result.success();
    }

    //删除(批量删除)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        ordersService.removeBatchByIds(ids);
        for (Integer id : ids) {
            //更新goods中库存数量
            QueryWrapper<OrdersGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", id);
            List<OrdersGoods> ordersGoodsList = ordersGoodsService.list(queryWrapper);
            for (OrdersGoods ordersGoods : ordersGoodsList) {
                Integer goodsId = ordersGoods.getGoodsId();
                Goods goods = goodsService.getById(goodsId);
                goods.setNum(ordersGoods.getNum());
                goods.setStore(String.valueOf(Integer.parseInt(goods.getStore()) + ordersGoods.getNum()));
                goodsService.saveNewStore(goods);
            }
        }
        return Result.success();
    }

    //信息(全部查询)
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Orders> Orderslist = ordersService.list(new QueryWrapper<Orders>().orderByDesc("id"));
        return Result.success(Orderslist);
    }

    //信息(单个查询，根据用户id)
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Orders Orders = ordersService.getById(id);
        return Result.success(Orders);
    }

    //信息(分页模糊查询)
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        User currentUser = TokenUtils.getCurrentUser();
        Page<Orders> page = ordersMapper.page(new Page<>(pageNum, pageSize), name, currentUser.getRole(), currentUser.getId());
        return Result.success(page);
    }

    @GetMapping("/getGoodsById/{id}")
    public Result getGoodsById(@PathVariable Integer id) {
        QueryWrapper<OrdersGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        ArrayList<Goods> goodsList = new ArrayList<>();
        List<OrdersGoods> ordersGoodsList = ordersGoodsService.list(queryWrapper);
        for (OrdersGoods ordersGoods : ordersGoodsList) {
            Integer goodsId = ordersGoods.getGoodsId();
            Goods goods = goodsService.getById(goodsId);
            goods.setNum(ordersGoods.getNum());
            goodsList.add(goods);
        }
        return Result.success(goodsList);
    }
}