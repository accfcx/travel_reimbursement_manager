package com.test.recipe.enums;

public enum ApplyType {

    TRAVEL_APPLY("chinaTravelApply", "差旅申请"),
    REMOTE_DISPATCH("remoteDispatch", "异地派遣"),
    ;

    String code;
    String desc;

    ApplyType(String code, String desc) {
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
