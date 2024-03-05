package com.test.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName TicketResult
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/31
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResult {
    // 车票编号
    private Integer tid;
    // 出发地
    private String startPoint;
    // 到达地
    private String arrivalPoint;
    // 出发时间
    private String startTime;
    // 到达时间
    private String arrivalTime;
    // 出差说明
    private String travelDesc;
    // 交通工具
    private String transportation;
    // 差旅编号
    private Integer travelId;
    // 车票价格
    private Double price;
    // 出差类型
    private String travelType;
    // 车票照片名称
    private String ticketPictureName;
}
