package com.test.recipe.enums;

// 单据状态
public enum RecipeStatus {
    INIT("unSubmit", "未提交"),
    APPROVAL("approval", "审批中"),
    REJECTED("deny", "已驳回"),
    FINISHED("finish", "已完成"),
    PAYED("payed", "已付款"),
    ;

    String code;
    String desc;

    RecipeStatus(String code, String desc) {
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
