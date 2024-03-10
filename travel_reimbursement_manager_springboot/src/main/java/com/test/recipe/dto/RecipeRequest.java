package com.test.recipe.dto;

import com.test.recipe.model.DailyReimbursement;
import com.test.recipe.model.OvertimeApply;
import com.test.recipe.model.TravelApply;
import com.test.recipe.model.TravelReimbursement;
import lombok.Data;

/**
 * @author accfcx
 **/
@Data
public class RecipeRequest {
    Long id;

    Long uid;

    String recipeType;

    TravelApply travelApply;

    OvertimeApply overtimeApply;

    TravelReimbursement travelReimbursement;

    DailyReimbursement dailyReimbursement;


}
