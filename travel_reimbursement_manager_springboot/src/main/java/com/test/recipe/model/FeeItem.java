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

    Date createTime;
    Date updateTime;

    String purpose;

    String startCity;
    String endCity;

    String month;
    Long peopleCount = 1L;
}