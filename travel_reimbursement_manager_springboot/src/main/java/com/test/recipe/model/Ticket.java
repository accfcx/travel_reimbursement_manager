package com.test.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Ticket
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/13
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    // 车票编号
    private Integer tid;
    // 员工编号
    private Integer empno;
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
    // 交通工具编号
    private Integer transId;
    // 差旅编号
    private Integer travelId;
    // 车票价格
    private Double price;
    // 出差类型编号
    private Integer travelTypeId;
    // 车票照片名称
    private String ticketPictureUrl;
}
