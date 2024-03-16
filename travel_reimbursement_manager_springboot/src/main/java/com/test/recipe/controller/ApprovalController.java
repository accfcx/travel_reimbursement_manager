package com.test.recipe.controller;

import com.test.recipe.dto.TaskDto;
import com.test.recipe.enums.RecipeStatus;
import com.test.recipe.enums.RecipeType;
import com.test.recipe.mapper.FeeItemMapper;
import com.test.recipe.mapper.PersonMapper;
import com.test.recipe.mapper.RecipeMapper;
import com.test.recipe.model.FeeItem;
import com.test.recipe.model.Person;
import com.test.recipe.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author accfcx
 **/
@RestController
@RequestMapping("/approval")
public class ApprovalController {

    @Resource
    PersonMapper personMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RecipeMapper recipeMapper;

    @Resource
    FeeItemMapper feeItemMapper;


    // 获取待本人审批的单据列表

    /**
     * 先找出 由我审批的流程实例列表
     * 每个流程实例 的业务属性 -  businessKey
     */
    @GetMapping("/queryRecipeTobeDoneByMe/{uid}")
    public List<TaskInfo> queryRecipeTobeDoneByMe(@PathVariable Long uid) {
        Person me = personMapper.findById(uid);
        String assignee = me.getUsername();

        List<TaskInfo> tasksForUser = getTasksForUser(assignee);

        return tasksForUser;
    }

    public List<TaskInfo> getTasksForUser(String assignee) {
        // 获取用户的待办任务
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        // 为每个任务获取接收时间和停留时长
        return tasks.stream().map(task -> {
            String taskId = task.getId();
            Date receiveTime = task.getCreateTime();
            long duration = getTaskDuration(task);

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            String businessKey = processInstance.getBusinessKey();
            String[] dataList = businessKey.split("##");
            String recipeId = dataList[1];

            Long id = Long.parseLong(recipeId);
            BigDecimal feeTotal = null;
            String reason = null;
            Long uid = 0l;
            Recipe recipe = recipeMapper.selectRecipe(id);
            feeTotal = recipe.getAmount();
            reason = recipe.getReason();
            uid = recipe.getUid();
            String username = personMapper.findById(uid).getNameZh();

            FeeItem feeItem = feeItemMapper.findByReceiptId(id);
            String feeType = null;
            if (Objects.nonNull(feeItem)) {
                feeType = feeItem.getFeeType();
            }

            // 业务类型、金额、提交人、事由、停留时长、接收时间、操作
            return new TaskInfo(id, processInstance.getProcessInstanceId(), taskId, recipe.getNo(), recipe.getRecipeType(), feeTotal, reason, username, duration, receiveTime, feeType);
        }).sorted(Comparator.comparing(TaskInfo::getReceiveTime).reversed()).collect(Collectors.toList());
    }

    private long getTaskDuration(Task task) {
        Date createTime = task.getCreateTime();
        Date now = new Date();

        return now.getTime() - createTime.getTime();
    }

    @GetMapping("/queryCompletedTasks/{uid}")
    public List<TaskInfo> queryCompletedTasks(@PathVariable Long uid) {
        Person me = personMapper.findById(uid);
        String assignee = me.getUsername();

        List<TaskInfo> completedTasks = getCompletedTasksForUser(assignee);

        return completedTasks;
    }

    public List<TaskInfo> getCompletedTasksForUser(String assignee) {
        // 查询用户已经完成的任务
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().taskAssignee(assignee).finished().list();

        // 为每个任务获取结束时间和处理时长
        return tasks.stream().map(task -> {
            String taskId = task.getId();
            Date endTime = task.getEndTime();
            long duration = task.getDurationInMillis();
            String processInstanceId = task.getProcessInstanceId();

            // 从流程实例中获取业务键
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String businessKey = processInstance.getBusinessKey();

            String[] dataList = businessKey.split("##");
            String type = dataList[0];
            String recipeId = dataList[1];

            Long id = Long.parseLong(recipeId);
            BigDecimal feeTotal = null;
            String reason = null;
            Long uid = 0l;
            Recipe recipe = recipeMapper.selectRecipe(id);
            feeTotal = recipe.getAmount();
            reason = recipe.getReason();
            uid = recipe.getUid();
            String username = personMapper.findById(uid).getNameZh();

            FeeItem feeItem = feeItemMapper.findByReceiptId(id);
            String feeType = null;
            if (Objects.nonNull(feeItem)) {
                feeType = feeItem.getFeeType();
            }


            return new TaskInfo(id, processInstanceId, taskId, recipe.getNo(), type, feeTotal, reason, username, duration, endTime, feeType);
        }).collect(Collectors.toList());
    }

    @PostMapping("/complete/{id}")
    public String complete(@PathVariable Long id, @RequestBody Map<String, Object> variables) {
        System.out.println(variables);

        Recipe recipe = recipeMapper.selectRecipe(id);

        Task task = taskService.createTaskQuery().processInstanceId(recipe.getProcessInstanceId()).singleResult();

        boolean isDeny = false;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            runtimeService.setVariable(task.getProcessInstanceId(), task.getTaskDefinitionKey() + "_SPLIT_" + entry.getKey(), entry.getValue());
            if (!(Boolean) variables.get("approvalResult")) {
                isDeny = true;
                String denyDetail = String.valueOf(variables.get("approvalResultDetail"));
                recipe.setRecipeStatus(RecipeStatus.REJECTED.getCode());
                recipe.setDenyDetail(task.getTaskDefinitionKey() + "_SPLIT_" + denyDetail);
                recipeMapper.updateRecipe(recipe);
            }
        }
        taskService.complete(task.getId());

        // 判断是否为最后一个流程
        if (!isDeny) {
            ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(recipe.getProcessInstanceId()).includeProcessVariables().singleResult();
            if (instance == null) {
                recipe.setRecipeStatus(RecipeStatus.PAYED.getCode());
                if (RecipeType.OVERTIME_APPLY.getCode().equals(recipe.getRecipeType()) || RecipeType.OVERTIME_APPLY.getCode().equals(recipe.getRecipeType())) {
                    recipe.setRecipeStatus(RecipeStatus.FINISHED.getCode());
                }
                recipeMapper.updateRecipe(recipe);
            }
        }
        return "审批结束";
    }

    @Data
    @AllArgsConstructor
    public class TaskInfo {
        Long id;
        String processInstanceId;
        String taskId;
        String no;
        String recipeType;
        BigDecimal amount;
        String reason;
        String username;
        long duration;
        Date receiveTime;
        String feeType;
    }

}
