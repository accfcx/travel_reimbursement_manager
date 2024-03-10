package com.test.recipe.model;

import lombok.Data;

@Data
public class DailyReimbursement {
    private Long id;
    private Long recipeId;
    private Long bankCardId;
    private String reason;
}
