package com.test.recipe.enums;

// 单据状态
public enum RecipeStatus {
    INIT(1, "未提交"),
    APPROVAL(2, "审批中"),
    REJECTED(3, "已驳回"),
    PAYED(4, "已付款"),
    ;

    Integer code;
    String desc;

    RecipeStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
