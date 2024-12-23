package com.xixi.user.common;

//系统日志的操作类型枚举

import lombok.Getter;

/**
 * @author 永
 */

@Getter
public enum LogType {
    ADD("新增"), UPDATE("修改"), DELETE("删除"), BATCH_DELETE("批量删除"), LOGIN("登录"), REGISTER("注册");
    private String value;


    LogType(String value) {
        this.value = value;
    }

}
