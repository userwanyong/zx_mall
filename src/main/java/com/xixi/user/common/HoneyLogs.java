package com.xixi.user.common;

import java.lang.annotation.*;

/**
 * @author 永
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HoneyLogs {
    //操作的模块
    String operation();

    //操作类型
    LogType type();
}
