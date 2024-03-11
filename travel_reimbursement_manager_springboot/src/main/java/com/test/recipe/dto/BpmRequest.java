package com.test.recipe.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author accfcx
 **/
@Data
public class BpmRequest {
    long id;
    String processName;
    String processKey;
    String recipeType;
    String processXml;
    Long submitUid;
    Long updateUid;
    int status;
    String processDefinitionKey;
    Date createTime;
    Date updateTime;
}
