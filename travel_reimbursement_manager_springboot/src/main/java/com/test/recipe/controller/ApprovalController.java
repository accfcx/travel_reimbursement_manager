package com.test.recipe.controller;

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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
                    HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();

                    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                    Date receiveTime = historicTask.getClaimTime();
                    long duration = getTaskDuration(historicTask);
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
                    String username = personMapper.findById(uid).getNamZh();

                    FeeItem feeItem = feeItemMapper.findByReceiptId(id);

                    // 业务类型、金额、提交人、事由、停留时长、接收时间、操作
                    return new TaskInfo(id, recipe.getNo(), type, feeTotal, reason, username, duration, receiveTime, feeItem.getFeeType());
                }).
                sorted(Comparator.comparing(TaskInfo::getReceiveTime).reversed()).
                collect(Collectors.toList());
    }

    private long getTaskDuration(HistoricTaskInstance task) {
        Date startTime = task.getStartTime();
        Date endTime = task.getEndTime();
        if (endTime != null) {
            return endTime.getTime() - startTime.getTime();
        } else {
            // 任务还没有结束，计算从开始到现在的时长
            return new Date().getTime() - startTime.getTime();
        }
    }

//    @GetMapping("/queryFinishedRecipeListForMe/{uid}")
//    public void getFinishedRecipeList(@PathVariable Long uid) {
//        Person person = personMapper.findById(uid);
//
//        String userId = person.getUsername();
////        List<HistoricTaskInstance> completedTasks = historyService.createHistoricTaskInstanceQuery()
////                .taskAssignee(userId)    // 指定审批人
////                .finished()    // 查询已完成的任务
////                .list();
//
//        // 4. 获取用户已完成的流程实例和businessKey
//        List<HistoricProcessInstance> completedProcesses = historyService.createHistoricProcessInstanceQuery()
//                .involvedUser(userId)    // 指定参与者
//                .finished()    // 查询已完成的流程实例
//                .list();
//
//        for (HistoricProcessInstance completedProcess : completedProcesses) {
//            String processInstanceId = completedProcess.getId();
//            String businessKey = completedProcess.getBusinessKey();
//            // 打印流程实例ID和businessKey
//            System.out.println("ProcessInstanceId: " + processInstanceId + ", BusinessKey: " + businessKey);
//        }
//    }

    @GetMapping("/queryCompletedTasks/{uid}")
    public List<TaskInfo> queryCompletedTasks(@PathVariable Long uid) {
        Person me = personMapper.findById(uid);
        String assignee = me.getUsername();

        List<TaskInfo> completedTasks = getCompletedTasksForUser(assignee);

        return completedTasks;
    }

    public List<TaskInfo> getCompletedTasksForUser(String assignee) {
        // 查询用户已经完成的任务
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .finished()
                .list();

        // 为每个任务获取结束时间和处理时长
        return tasks.stream().map(task -> {
            String taskId = task.getId();
            Date endTime = task.getEndTime();
            long duration = task.getDurationInMillis();
            String processInstanceId = task.getProcessInstanceId();

            // 从流程实例中获取业务键
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
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
            String username = personMapper.findById(uid).getNamZh();

            FeeItem feeItem = feeItemMapper.findByReceiptId(id);

            return new TaskInfo(id, recipe.getNo(), type, feeTotal, reason, username, duration, endTime, feeItem.getFeeType());
        }).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    public class TaskInfo {
        Long id;
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
