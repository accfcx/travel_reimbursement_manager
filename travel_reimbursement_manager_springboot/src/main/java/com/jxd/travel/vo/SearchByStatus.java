package com.jxd.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SearchByStatus
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/4
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchByStatus {
    private int empno;
    private Integer page;
    private Integer limit;
    private Integer status;
}
