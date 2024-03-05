package com.test.recipe.dto;

import com.test.recipe.model.SubsidyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ReimbursementResult
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/4
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReimbursementResult {
    // 补助信息
    private List<SubsidyInfo> subsidyForm;
    // 差旅信息
    private TravelInfoResult travelForm;
    // 待关联的车票
    private List<TicketResult> tickets;
}
