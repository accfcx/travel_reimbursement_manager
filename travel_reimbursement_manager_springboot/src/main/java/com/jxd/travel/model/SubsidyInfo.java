package com.jxd.travel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SubsidyInfo
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/2
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsidyInfo {
    // 补助信息编号
    private Integer sid;
    // 开始时间
    private String startTime;
    // 截止时间
    private String endTime;
    // 工作地
    private String workPoint;
    // 补助信息对应的差旅信息id
    private Integer travelId;
    // 补贴金额
    private Double subsidy;
    // 补助天数，即出差天数
    private Double travelDays;
}
