package com.jxd.travel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @ClassName User
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/28
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 用户id
    private Integer uid;
    // 登录密码
    private String password;
}
