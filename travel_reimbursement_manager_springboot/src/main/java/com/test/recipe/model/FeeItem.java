package com.test.recipe.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class FeeItem {
    Long id;
    String type;

    String purpose;
    String carType;

    String startCity;

    String endCity;

    String city;

    String dateList;

    BigDecimal amount;
    String currency;
    String remark;

    Long receiptId;


    Date createTime;

    Date updateTime;
}