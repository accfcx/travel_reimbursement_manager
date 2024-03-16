package com.test.recipe.enums;

// 差旅申请类型
public enum TravelApplyType {

    TRAVEL_APPLY("chinaTravelApply", "差旅申请"),
    REMOTE_DISPATCH("remoteDispatch", "异地派遣"),
    ;

    String code;
    String desc;

    TravelApplyType(String code, String desc) {
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
