package com.xixi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 永
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logs {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String operation;
    private String type;
    private String ip;
    private String user;
    private String time;
    private String methodName;
}
