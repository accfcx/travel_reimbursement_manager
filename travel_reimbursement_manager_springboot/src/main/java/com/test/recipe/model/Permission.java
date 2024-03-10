package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class Permission {
    Long id;

    String name;

    String role;


    Date createTime;

    Date updateTime;
}