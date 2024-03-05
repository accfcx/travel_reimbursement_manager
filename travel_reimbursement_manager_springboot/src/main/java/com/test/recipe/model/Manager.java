package com.test.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Manager
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/13
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    // 管理员编号
    private Integer mno;
    // 管理员姓名
    private String mname;
}
