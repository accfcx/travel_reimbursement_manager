package com.test.recipe.model;

import lombok.Data;

@Data
public class TravelReimbursement {
    private Long id;
    private Long recipeId;
    private Long applicationId;
    private Long bankCardId;
    private String purpose;
}