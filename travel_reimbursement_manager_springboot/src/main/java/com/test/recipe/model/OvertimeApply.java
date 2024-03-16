package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

@Data
public class OvertimeApply {
    private Long id;
    private Long recipeId;
    private Date startDate;
    private Date endDate;
    private Integer hours;

    Date createTime;

    Date updateTime;
}
