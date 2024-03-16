package com.test.recipe.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Recipe {
    Long id;
    String no;
    String recipeType;
    BigDecimal amount;
    Long uid;
    Long departmentId;
    String reason;
    String recipeStatus;
    String denyDetail;
    String processInstanceId;

    Date createTime;
    Date updateTime;

    Date createTimeStart;
    Date createTimeEnd;
    List<String> recipeTypeList;
}
