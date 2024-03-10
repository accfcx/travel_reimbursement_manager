package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class BankCard {
    Long id;

    Long ownerId;

    String cardNumber;

    String bank;

    String area;

    Date createTime;

    Date updateTime;
}