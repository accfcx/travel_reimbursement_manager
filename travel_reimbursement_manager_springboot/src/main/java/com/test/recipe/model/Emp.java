package com.test.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Emp
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/13
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp {
    // 员工编号
    private Integer empno;
    // 员工姓名
    private String ename;
    // 员工性别
    private String sex;
    // 员工电话
    private String tel;
}
