package com.jxd.travel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName TravelType
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/31
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelType {
    // 出差方式id
    private Integer travelTypeId;
    // 出差方式名称
    private String travelTypeName;
}
