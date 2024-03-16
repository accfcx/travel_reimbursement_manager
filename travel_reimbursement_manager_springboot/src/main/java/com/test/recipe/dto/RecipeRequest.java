package com.test.recipe.dto;

import com.test.recipe.enums.FeeType;
import com.test.recipe.model.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class RecipeRequest {
    Long id;
    String no;
    String recipeType;
    BigDecimal amount = BigDecimal.ZERO;
    Long uid;
    Long departmentId;
    String reason;
    String recipeStatus;
    String denyDetail;

    String processInstanceId;

    Date createTimeStart;
    Date createTimeEnd;

    TravelApply travelApply;

    OvertimeApply overtimeApply;

    TravelReimbursement travelReimbursement;

    DailyReimbursement dailyReimbursement;

    FeeItem feeItem;

    // 报销单/申请单
    String queryRecipeType;

}
