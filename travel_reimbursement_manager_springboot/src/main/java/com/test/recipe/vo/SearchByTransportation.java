package com.test.recipe.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SearchByTransportation
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/5
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchByTransportation {
    private String transportation;
    private Integer page;
    private Integer limit;
}
