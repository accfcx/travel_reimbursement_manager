
package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class Department {
    Long id;

    Long parentId;

    String name;


    Date createTime;

    Date updateTime;
}