package com.test.recipe.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class FeeItem {
    Long id;
    Long recipeId;
    String feeType;
    Date startDate;
    Date endDate;
    BigDecimal amount;
    String startCity;
    String endCity;
    String purpose;
    int peopleCount;

    Date createTime;

    Date updateTime;
}