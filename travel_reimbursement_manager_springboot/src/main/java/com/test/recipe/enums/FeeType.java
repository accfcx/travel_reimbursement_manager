package com.test.recipe.enums;

// 费用项类型
public enum FeeType {
    Transportation_Fee("transportationFee", "交通费用"),
    Team_Building("teamBuilding", "部门团建"),
    Travel_Reimbursement("travelReimbursement", "差旅报销"),
    ;

    String code;
    String desc;

    FeeType(String code, String desc) {
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
