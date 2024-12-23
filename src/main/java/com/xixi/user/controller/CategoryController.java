package com.xixi.user.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.HoneyLogs;
import com.xixi.user.common.LogType;
import com.xixi.user.common.Result;
import com.xixi.user.entity.Category;
import com.xixi.user.entity.User;
import com.xixi.user.service.CategoryService;
import com.xixi.user.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //增
    @HoneyLogs(operation = "分类", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Category category) {
        User currentUser = TokenUtils.getCurrentUser();
        category.setCreateUser(currentUser.getUsername());
        category.setTime(DateUtil.now());
        categoryService.save(category);
        return Result.success();
    }

    //改
    @HoneyLogs(operation = "分类", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success();
    }


    //删（根据id）
    @HoneyLogs(operation = "分类", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        categoryService.removeById(id);
        return Result.success();
    }

    //删(批量删除)
    @HoneyLogs(operation = "分类", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> categoryIds) {
        categoryService.removeBatchByIds(categoryIds);
        return Result.success();
    }

    //查询全部
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Category> userlist = categoryService.list(new QueryWrapper<Category>().orderByDesc("id"));
        return Result.success(userlist);
    }


    //信息(分页模糊查询)
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>().orderByDesc("id");//让最新的数据在最上面
        Page<Category> page = categoryService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }


    //查询分类名
    @GetMapping("/selectByCategoryName")
    public Result selectByCategoryName() {
        List<String> categoryNameList = categoryService.selectByCategoryName();
        return Result.success(categoryNameList);
    }

}
