package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

@Data
public class TravelApply {
    Long id;
    Long recipeId;
    String applyType;
    String travelTarget;
    Date startDate;
    Date endDate;

    Date createTime;

    Date updateTime;
}
