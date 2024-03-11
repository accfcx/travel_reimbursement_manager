package com.test.recipe.dto;

import com.test.recipe.model.DailyReimbursement;
import com.test.recipe.model.OvertimeApply;
import com.test.recipe.model.TravelApply;
import com.test.recipe.model.TravelReimbursement;
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
    BigDecimal amount;
    Long uid;
    Long departmentId;
    String reason;
    String recipeStatus;

    String processInstanceId;

    Date createTimeStart;
    Date createTimeEnd;

    TravelApply travelApply;

    OvertimeApply overtimeApply;

    TravelReimbursement travelReimbursement;

    DailyReimbursement dailyReimbursement;

    // 报销单/申请单
    String queryRecipeType;

}
