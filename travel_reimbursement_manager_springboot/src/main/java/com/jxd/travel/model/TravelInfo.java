package com.jxd.travel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName TravelInfo
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/13
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelInfo {
    // 差旅编号
    private Integer travelId;
    // 申请差旅报销的员工工号
    private Integer empno;
    // 出发时间
    private String startTime;
    // 到达时间
    private String arrivalTime;
    // 出差天数
    private Double travelDays;
    // 出差说明
    private String travelDesc;
    // 报销时间
    private String reimbursementDate;
    // 报销总额
    private Double reimbursementTotal;
    // 差旅信息的状态（保存、提交、审核）
    private Double status;
    // 报销金额大写
    private String moneyUppercase;
    // 驳回原因
    private String rejectionReason;
}
