package com.jxd.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName PassForm
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/4
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassForm {
    List<Integer> travelIds;
    Integer uid;

}
