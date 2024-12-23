package com.xixi.user.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.Result;
import com.xixi.user.entity.Logs;
import com.xixi.user.service.LogsService;
import com.xixi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/logs")
public class LogsController {

    @Autowired
    LogsService logsService;

    @Autowired
    UserService userService;

    //删除信息
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        logsService.removeById(id);
        return Result.success();
    }

    //删除信息(批量删除)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        logsService.removeBatchByIds(ids);
        return Result.success();
    }

    //信息(分页模糊查询)
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String type,
                               @RequestParam String user,
                               @RequestParam String operation) {
        QueryWrapper<Logs> queryWrapper = new QueryWrapper<Logs>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(type), "type", type);//查询的依据
        queryWrapper.like(StrUtil.isNotBlank(user), "user", user);//查询的依据
        queryWrapper.like(StrUtil.isNotBlank(operation), "operation", operation);//查询的依据
        Page<Logs> page = logsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }
}
