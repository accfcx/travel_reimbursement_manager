package com.test.recipe.model;

import lombok.Data;

import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class ProcessDef {
    long id;
    String processName;
    String processKey;
    String recipeType;
    String processXml;
    Long submitUid;
    Long updateUid;
    int status;
    Date createTime;
    Date updateTime;
}