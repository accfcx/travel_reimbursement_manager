package com.jxd.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SearchTicket
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/30
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchByTime {
    private Integer empno;
    private Integer page;
    private Integer limit;
    private String startTime;
    private String arrivalTime;
    private String reimbursementDate;
}
