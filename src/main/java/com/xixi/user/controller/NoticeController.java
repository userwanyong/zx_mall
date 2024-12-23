package com.xixi.user.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.HoneyLogs;
import com.xixi.user.common.LogType;
import com.xixi.user.common.Result;
import com.xixi.user.entity.Notice;
import com.xixi.user.entity.User;
import com.xixi.user.service.NoticeService;
import com.xixi.user.service.UserService;
import com.xixi.user.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeController {


    @Autowired
    NoticeService noticeService;


    @Autowired
    UserService userService;


    //新增信息
    @HoneyLogs(operation = "公告", type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        User currentUser = TokenUtils.getCurrentUser();//获取当前登录用户信息
        notice.setUserid(currentUser.getId());
        notice.setCtime(DateUtil.now());
        notice.setUtime(DateUtil.now());
        noticeService.save(notice);
        return Result.success();
    }

    //修改信息
    @HoneyLogs(operation = "公告", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {
        notice.setUtime(DateUtil.now());
        noticeService.updateById(notice);
        return Result.success();
    }

    //删除信息
    @HoneyLogs(operation = "公告", type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.removeById(id);
        return Result.success();
    }

    //删除信息(批量删除)
    @HoneyLogs(operation = "公告", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        noticeService.removeBatchByIds(ids);
        return Result.success();
    }

    //查询全部
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Notice> userlist = noticeService.list(new QueryWrapper<Notice>().orderByDesc("id"));
        return Result.success(userlist);
    }


    //查询用户公告
    @GetMapping("/selectUserData")
    public Result selectUserData() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByDesc("id");
        queryWrapper.eq("open", 1);//用户只能看到公开的公告
        List<Notice> userlist = noticeService.list(queryWrapper);
        return Result.success(userlist);
    }


    //单个查询(根据id)
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notice notice = noticeService.getById(id);
        User user = userService.getById(notice.getUserid());
        if (user != null) {
            notice.setUser(user.getUsername());
        }
        return Result.success(notice);
    }


    //信息(分页模糊查询)
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String title) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(title), "title", title);//查询的依据
        Page<Notice> page = noticeService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Notice> records = page.getRecords();
        for (Notice record : records) {
            Integer authorid = record.getUserid();
            User user = userService.getById(authorid);
            if (user != null) {
                record.setUser(user.getName());
            }
        }
        return Result.success(page);
    }


}
