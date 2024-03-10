package com.test.recipe.enums;

// 差旅目的
public enum TravelTarget {
    BUSINESS_TRIP("businessTrip", "普通差旅"),
    Internal_Meeting("internalMeeting", "内部会议"),
    ;

    String code;
    String desc;

    TravelTarget(String code, String desc) {
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
