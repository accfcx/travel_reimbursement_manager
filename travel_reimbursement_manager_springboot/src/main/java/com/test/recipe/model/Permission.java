package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class Permission {
    Long id;

    Long uid;

    String role;


    Date createTime;

    Date updateTime;
}