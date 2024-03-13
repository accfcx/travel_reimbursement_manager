package com.test.recipe.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票 - 一张报销单关联多张发票
 * @author accfcx
 **/
@Data
public class Invoice {
    Long id;
    String invoiceNumber;
    BigDecimal amount;
    String invoiceDate;
    String invoiceType;

    Long recipeId;

    Date createTime;

    Date updateTime;
}