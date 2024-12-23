package com.xixi.user.controller;

import cn.hutool.core.util.StrUtil;
import com.xixi.user.common.AuthAccess;
import com.xixi.user.common.HoneyLogs;
import com.xixi.user.common.LogType;
import com.xixi.user.common.Result;
import com.xixi.user.entity.User;
import com.xixi.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author 永
 */
@CrossOrigin
@RestController
public class WebController {
    @Resource
    UserService userService;

    @AuthAccess
    @GetMapping("/")
    public Result hello() {
        return Result.success("success");
    }


    @HoneyLogs(operation = "用户", type = LogType.LOGIN)
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }
        user = userService.login(user);
        return Result.success(user);
    }

    @HoneyLogs(operation = "用户", type = LogType.REGISTER)
    @AuthAccess
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getRole())) {
            return Result.error("数据输入不合法");
        }
        if (user.getUsername().length() > 15 || user.getPassword().length() > 20) {
            return Result.error("请输入规定的账号/密码的长度(账号<=15，密码<=20)");
        }
        user = userService.register(user);
        return Result.success(user);
    }


    @HoneyLogs(operation = "用户", type = LogType.UPDATE)
    //重置密码
    @AuthAccess
    @PutMapping("/password")
    public Result password(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || (StrUtil.isBlank(user.getPhone()) && StrUtil.isBlank(user.getEmail()))) {
            return Result.error("数据输入不合法");
        }
        userService.resePassword(user);
        return Result.success();
    }

}
