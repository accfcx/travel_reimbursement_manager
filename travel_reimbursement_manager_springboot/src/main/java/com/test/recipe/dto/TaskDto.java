package com.test.recipe.dto;

import lombok.Data;
import org.activiti.engine.task.Task;

import java.util.Date;

@Data
public class TaskDto {

    String activityId;

    String taskInstanceId;

    String taskName;

    String processInstanceId;

    String assignee;

    Date createTime;

    Date claimTime;

    String businessKey;

    public TaskDto(Task task) {
        this.activityId = task.getTaskDefinitionKey();
        this.taskInstanceId = task.getId();
        this.assignee = task.getAssignee();
        this.createTime = task.getCreateTime();
        this.claimTime = task.getClaimTime();
        this.businessKey = task.getBusinessKey();
        this.taskName = task.getName();
        this.processInstanceId = task.getProcessInstanceId();
    }

    public TaskDto() {
    }
}
