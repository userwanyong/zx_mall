package com.xixi.user.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.xixi.user.entity.User;
import com.xixi.user.exception.ServiceException;
import com.xixi.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author 永
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper UserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");//header里面传过来的参数
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");//url参数
        }
        //如果不是映射到方法直接通过
        if (handler instanceof HandlerMethod) {
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null) {
                return true;
            }
        }

        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401", "请登录");
        }

        // 获取 token 中的 userid
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);//JWT.decode(token)  解码jwt token
        } catch (JWTDecodeException j) {
            throw new ServiceException("401", "请登录");
        }
        //根据token中的userid查询数据库
        User user = UserMapper.selectById(Integer.valueOf(userId));
        if (user == null) {
            throw new ServiceException("401", "请登录");
        }
        // 通过用户密码加密之后生成一个验证器
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException("401", "请登录");
        }
        return true;
    }
}
