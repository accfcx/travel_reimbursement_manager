package com.jxd.travel.vo;

import com.jxd.travel.dto.TicketResult;
import com.jxd.travel.model.SubsidyInfo;
import com.jxd.travel.model.TravelInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName AssociateTickets
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/2
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssociateTickets {
    // 补助信息
    private List<SubsidyInfo> subsidyForm;
    // 差旅信息
    private TravelInfo travelForm;
    // 待关联的车票
    private List<TicketResult> tickets;
}
