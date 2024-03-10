package com.test.recipe.dto;

import lombok.Data;

/**
 * @author accfcx
 **/
@Data
public class BpmRequest {

    long id;

    // 流程模版定义xml
    String xmlStr;

    // 流程模型名称
    String modelName;

    // 流程模型主键key
    String modelKey;

    // 创建人
    String user;


    int status;
}
