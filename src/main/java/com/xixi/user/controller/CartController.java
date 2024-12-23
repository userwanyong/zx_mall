package com.xixi.user.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.AuthAccess;
import com.xixi.user.common.Result;
import com.xixi.user.entity.Cart;
import com.xixi.user.entity.User;
import com.xixi.user.mapper.CartMapper;
import com.xixi.user.service.CartService;
import com.xixi.user.service.GoodsService;
import com.xixi.user.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/Cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Resource
    CartMapper cartMapper;


    //增
    @PostMapping("/add")
    public Result add(@RequestBody Cart cart) {
        Integer userId = TokenUtils.getCurrentUser().getId();

        //更新购物车里相同商品的数量
        Integer goodsId = cart.getGoodsId();
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("goods_id", goodsId);
        Cart db = cartService.getOne(queryWrapper);
        if (db != null) {
            db.setNum(db.getNum() + cart.getNum());//更新商品数量
            cartService.updateById(db);
            return Result.success();
        }

        //新增
        cart.setTime(DateUtil.now());
        cart.setUserId(userId);
        cartService.save(cart);
        return Result.success();
    }

    //改(根据id修改)

    @PutMapping("/update")
    public Result update(@RequestBody Cart Cart) {
        cartService.updateById(Cart);
        return Result.success();
    }

    //删(单个删除)

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        cartService.removeById(id);
        return Result.success();
    }

    //删除(批量删除)

    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        cartService.removeBatchByIds(ids);
        return Result.success();
    }

    //信息(全部查询)
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Cart> Cartlist = cartService.list(new QueryWrapper<Cart>().orderByDesc("id"));
        return Result.success(Cartlist);
    }


    //信息(单个查询，根据用户id)
    @AuthAccess
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Cart Cart = cartService.getById(id);
        return Result.success(Cart);
    }


    //信息(分页模糊查询)
//    @AuthAccess//用于忽略token限制
//    @GetMapping("/selectByPage")
//    public Result selectByPage(@RequestParam Integer pageNum,
//                               @RequestParam Integer pageSize,
//                               @RequestParam String goodsId) {
//        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>().orderByDesc("id");//让最新的数据在最上面
//        queryWrapper.like(StrUtil.isNotBlank(goodsId),"goods_id",goodsId);
//        Page<Cart> page = cartService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        return Result.success(page);
//    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") Integer goodsId) {
        User currentUser = TokenUtils.getCurrentUser();
        Page<Cart> page = cartMapper.page(new Page<>(pageNum, pageSize), goodsId, currentUser.getRole(), currentUser.getId());
        return Result.success(page);
    }
}
