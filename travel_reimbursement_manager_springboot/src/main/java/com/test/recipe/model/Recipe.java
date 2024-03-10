package com.test.recipe.model;

import lombok.Data;

import java.math.BigDecimal;

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
}
