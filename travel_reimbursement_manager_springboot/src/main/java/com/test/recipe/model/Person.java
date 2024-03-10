package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class Person {

    Long id;

    String username;

    String namZh;

    String password;

    Long departmentId;


    Date createTime;

    Date updateTime;
}