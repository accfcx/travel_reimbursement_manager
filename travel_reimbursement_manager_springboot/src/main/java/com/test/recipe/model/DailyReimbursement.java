package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

@Data
public class DailyReimbursement {
    private Long id;
    private Long recipeId;
    private Long bankCardId;

    Date createTime;

    Date updateTime;
}
