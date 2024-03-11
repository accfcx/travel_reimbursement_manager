package com.test.recipe.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Recipe {
    private Long id;
    private String no;
    private String recipeType;
    private BigDecimal amount;
    private Long uid;
    private Long departmentId;
    private String reason;
    private String recipeStatus;
    String denyDetail;
    String processInstanceId;

    Date createTime;
    Date updateTime;

    Date createTimeStart;
    Date createTimeEnd;
    List<String> recipeTypeList;
}
