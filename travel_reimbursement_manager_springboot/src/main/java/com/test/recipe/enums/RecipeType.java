package com.test.recipe.enums;


// 单据类型
public enum RecipeType {
    TRAVEL_APPLY("travelApply", "差旅申请"),
    TRAVEL_REIMBURSEMENT("travelReimbursement", "差旅报销"),
    OVERTIME_APPLY("overtimeApply", "加班申请"),
    DAILY_REIMBURSEMENT("dailyReimbursement", "日常报销"),
    ;

    String code;
    String desc;

    RecipeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
