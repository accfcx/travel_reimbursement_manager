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
    String publishProcessXml;
    Long submitUid;
    String submitName;
    Long updateUid;
    String updateName;

    int status;
    String processDefinitionKey = "";
    String processDefinitionId = "";
    String variables = "";
    String publishVariables = "";
    Date createTime;
    Date updateTime;
}