package com.jxd.travel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName DeptHead
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/13
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptHead {
    // 经理编号
    private Integer hid;
    // 经理姓名
    private String hname;
}
