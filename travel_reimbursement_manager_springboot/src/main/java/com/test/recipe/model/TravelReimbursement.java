package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

@Data
public class TravelReimbursement {
    Long id;
    Long recipeId;
    Long relatedTravelApplyId;
    Long bankCardId;

    String travelTarget;

    Date createTime;

    Date updateTime;
}