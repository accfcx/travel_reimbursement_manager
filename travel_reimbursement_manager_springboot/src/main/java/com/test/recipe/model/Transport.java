package com.test.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Transportation
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/31
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transport {
    // 交通工具编号
    private Integer transId;
    // 交通工具名称
    private String transportation;
}
