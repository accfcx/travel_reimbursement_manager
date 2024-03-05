package com.test.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Financial
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/1
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Financial {
    // 财务部员工编号
    private Integer fid;
    // 财务部员工姓名
    private String fname;
}
