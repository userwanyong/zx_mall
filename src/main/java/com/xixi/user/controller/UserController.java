package com.xixi.user.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.user.common.HoneyLogs;
import com.xixi.user.common.LogType;
import com.xixi.user.common.Result;
import com.xixi.user.entity.User;
import com.xixi.user.exception.ServiceException;
import com.xixi.user.service.UserService;
import com.xixi.user.utils.TokenUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 永
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    @HoneyLogs(operation = "用户", type = LogType.ADD)
    //新增用户信息
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.save(user);
        return Result.success();
    }

    @HoneyLogs(operation = "用户", type = LogType.UPDATE)
    //修改用户信息(根据id修改)
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    @HoneyLogs(operation = "用户", type = LogType.UPDATE)
    //修改用户信息(根据id修改)
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody User user) {
        userService.updatePassword(user);
        return Result.success();
    }

    @HoneyLogs(operation = "用户", type = LogType.DELETE)
    //删除用户信息(单个删除)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        if (id.equals(currentUser.getId())) {
            throw new ServiceException("不能删除当前用户");
        }
        userService.removeById(id);
        return Result.success();
    }

    @HoneyLogs(operation = "用户", type = LogType.BATCH_DELETE)
    //删除用户信息(批量删除)
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null && ids.contains(currentUser.getId())) {
            throw new ServiceException("不能删除当前用户");
        }
        userService.removeBatchByIds(ids);
        return Result.success();
    }

    //用户信息(全部查询)
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userlist = userService.list(new QueryWrapper<User>().orderByDesc("id"));
        return Result.success(userlist);
    }

    //用户信息(单个查询，根据用户id)
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }


    //用户信息(分页模糊查询)
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String username, @RequestParam String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");//让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }


    //导出
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String ids,
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<User> list;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)) {
            List<Integer> idsArr1 = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id", idsArr1);
        } else {
            //全部导出
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }
        list = userService.list(queryWrapper);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.flush();
        outputStream.close();
    }

    //导入
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        //写入数据到数据库
        try {
            userService.saveBatch(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("包含已有用户，无法导入");
        }
        return Result.success();
    }


}
