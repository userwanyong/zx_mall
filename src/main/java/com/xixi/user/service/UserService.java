package com.xixi.user.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixi.user.entity.User;
import com.xixi.user.exception.ServiceException;
import com.xixi.user.mapper.UserMapper;
import com.xixi.user.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 永
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Resource
    UserMapper userMapper;


    @Override
    public boolean save(User entity) {//用户信息的默认值
        if (StrUtil.isBlank(entity.getName())) {
            entity.setName(entity.getUsername());
        }
        if (StrUtil.isBlank(entity.getPassword())) {
            entity.setPassword("123");//默认密码123
        }
        if (StrUtil.isBlank(entity.getRole())) {
            entity.setRole("用户");//默认角色‘用户’
        }
        return super.save(entity);
    }


    public User selectByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        //根据用户名查询数据库的用户信息
        return userMapper.selectOne(queryWrapper);
    }

    //验证用户账户是否合法
    public User login(User user) {
        User dbUser = selectByUsername(user.getUsername());//根据用户名查询数据库的用户信息
        if (dbUser == null) {
            //抛出一个自定义的异常
            throw new ServiceException("用户名或密码错误");
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        //生成token
        String token = TokenUtils.creatToken(dbUser.getId().toString(), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public User register(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if (dbUser != null) {
            //抛出一个自定义的异常
            throw new ServiceException("用户名已存在");
        }
        user.setName(user.getUsername());
        userMapper.insert(user);
        return null;
    }

    public void resePassword(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if (dbUser == null) {
            //抛出一个自定义的异常
            throw new ServiceException("用户不存在");
        }
        if (!user.getPhone().equals(dbUser.getPhone()) && !user.getEmail().equals(dbUser.getEmail())) {
            throw new ServiceException("验证错误");
        }
        dbUser.setPassword("123");
        updateById(dbUser);
    }

    public void updatePassword(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if (!user.getOldPassword().equals(dbUser.getPassword())) {
            throw new ServiceException("原密码错误");
        }
        if (!user.getNewPassword().equals(user.getConfirmPassword())) {
            throw new ServiceException("两次输入密码不一致");
        }
        dbUser.setPassword(user.getNewPassword());
        updateById(dbUser);
    }
}
