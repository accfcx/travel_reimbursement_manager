package com.test.recipe.controller;

import com.test.recipe.mapper.ProcessDefMapper;
import com.test.recipe.dto.BpmRequest;
import com.test.recipe.model.ProcessDef;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ActivitController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessDefMapper processDefMapper;

    @Autowired
    private HistoryService historyService;


    @GetMapping("/definition/{processDefinitionKey}")
    public String getProcessDefinitionXML(@PathVariable String processDefinitionKey) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        String processDefinitionId = processDefinition.getId();
        InputStream xmlStream = repositoryService.getProcessModel(processDefinitionId);
        String xml = new Scanner(xmlStream, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
        return xml;
    }

    @GetMapping("/getForm")
    public List<Map<String, Object>> getForm(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //获取task对应的表单内容 需要TaskDefinitionKey
        UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId()).getFlowElement(task.getTaskDefinitionKey());
        //外部表单
        //String formKey = userTask.getFormKey();

        List<FormProperty> formProperties = userTask.getFormProperties();

        List<Map<String, Object>> collect = formProperties.stream().map(formProperty -> {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("id", formProperty.getId());
            map.put("type", formProperty.getType());
            map.put("name", formProperty.getName());
            map.put("defaultValue", formProperty.getDefaultExpression());

            //在camunda叫做label 前端转json加了一个name属性 不然取不到值
            //在camunda叫做defaultValue activiti表单的default 前端转json加了一个default属性 后台对应defaultExpression
            //type = enum  枚举类型会用
//                    .setAttribute("formValues", formProperty.getFormValues())
            //下面没有值 camunda和activiti表单有区别
//                    .setAttribute("variable", formProperty.getVariable())
//                    .setAttribute("expression", formProperty.getExpression())
//                    .setAttribute("datePattern", formProperty.getDatePattern())
            return map;
        }).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/getFormData")
    public void getFormData(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //获取task对应的表单内容 需要TaskDefinitionKey
        UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId()).getFlowElement(task.getTaskDefinitionKey());

        List<FormProperty> formProperties = userTask.getFormProperties();
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(@RequestParam String processDefinitionKey, @RequestParam String assignee) {
        if (StringUtils.isNotEmpty(processDefinitionKey)) {
            return taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskAssignee(assignee).list();
        }
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    @GetMapping("/task/{processInstanceId}/{taskDefinitionKey}")
    public String getTaskId(@PathVariable String processInstanceId, @PathVariable String taskDefinitionKey) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(taskDefinitionKey).singleResult();
        return task != null ? task.getId() : null;
    }

    @GetMapping("/task/todo")
    public List getTodoTaskForUser(@RequestParam String user) {
        return null;
    }

    @PostMapping("/task/complete/{taskId}")
    public void completeOneTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    @GetMapping("/task/history")
    public void getTaskHistory(@RequestParam String processDefinitionKey, @RequestParam String processInstanceId, @RequestParam String taskAssignee) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().activityType("userTask")//只获取用户任务
                .processInstanceId(processInstanceId).taskAssignee(taskAssignee).finished().list();
        for (HistoricActivityInstance instance : list) {
            System.out.println("任务名称:" + instance.getActivityName());
            System.out.println("任务开始时间:" + instance.getStartTime());
            System.out.println("任务结束时间:" + instance.getEndTime());
            System.out.println("任务耗时:" + instance.getDurationInMillis());
            List<Comment> taskComments = taskService.getTaskComments(instance.getTaskId());
            if (taskComments.size() > 0) {
                System.out.println("审批批注:" + taskComments.get(0).getFullMessage());
            }
        }
    }

    @GetMapping("/tasks/{processInstanceId}")
    public List<String> getTaskList(@PathVariable String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return tasks.stream().map(task -> "Task ID: " + task.getId() + ", Task Name: " + task.getName() + ", Assignee: " + task.getAssignee()).collect(Collectors.toList());
    }

    @GetMapping("/sequences/{processInstanceId}")
    public List<String> getSequences(@PathVariable String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
        List<SequenceFlow> sequenceFlows = ((org.activiti.bpmn.model.Process) repositoryService.getBpmnModel(processDefinition.getId()).getMainProcess()).findFlowElementsOfType(org.activiti.bpmn.model.SequenceFlow.class);
        return sequenceFlows.stream().map(SequenceFlow::getId).collect(Collectors.toList());
    }

    @PostMapping("/complete")
    public String completeTask(@RequestParam String taskId, @RequestBody Map<String, Object> variables) {
        taskService.complete(taskId, variables);
        return "Task completed";
    }


    @GetMapping("/instances")
    public List<Map<String, Object>> getProcessInstances(@RequestParam String processDefinitionKey) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).list();

        List<Map<String, Object>> customTaskList = new ArrayList<>();
        for (ProcessInstance instance : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("instanceId", instance.getId());
            map.put("businessKey", instance.getBusinessKey());
            map.put("name", instance.getName());
            map.put("deploymentId", instance.getDeploymentId());
            map.put("processDefinitionId", instance.getProcessDefinitionId());
            map.put("startTime", instance.getStartTime());
            map.put("startUserId", instance.getStartUserId());

            // 获取当前活动节点的 ID
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(instance.getId());
            map.put("activeActivityIds", activeActivityIds);

            customTaskList.add(map);
        }
        return customTaskList;
    }

    @GetMapping("/instance/{processInstanceId}")
    public Map<String, Object> getProcessInstance(@PathVariable String processInstanceId) {

        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("instanceId", instance.getId());
        map.put("businessKey", instance.getBusinessKey());
        map.put("name", instance.getName());
        map.put("deploymentId", instance.getDeploymentId());
        map.put("processDefinitionId", instance.getProcessDefinitionId());
        map.put("startTime", instance.getStartTime());
        map.put("startUserId", instance.getStartUserId());

        // 获取当前活动节点的 ID
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(instance.getId());

        // 获取已完成节点的id - 生成的task实例id
        List<String> finishedTaskIdList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished().list().stream().map(TaskInfo::getId).collect(Collectors.toList());

        // 转换为xml 定义的id
        List<String> finishedIdList = finishedTaskIdList.stream().map(taskId -> {
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            String xmlId = historicTaskInstance.getTaskDefinitionKey();
            return xmlId;
        }).collect(Collectors.toList());

        List<String> lineIdList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityType("sequenceFlow").list().stream().map(item -> item.getId()).collect(Collectors.toList());

        map.put("activeActivityIds", activeActivityIds);
        map.put("finishedTaskList", finishedIdList);

        return map;
    }

    @GetMapping("/instanceList")
    public List<Map<String, Object>> instanceList() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        List<Map<String, Object>> customTaskList = new ArrayList<>();
        for (ProcessInstance instance : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("businessKey", instance.getBusinessKey());
            map.put("name", instance.getName());
            map.put("id", instance.getId());
            map.put("processInstanceId", instance.getProcessInstanceId());
            map.put("deploymentId", instance.getDeploymentId());
            map.put("processDefinitionId", instance.getProcessDefinitionId());
            map.put("processDefinitionKey", instance.getProcessDefinitionKey());
            map.put("isEnded", instance.isEnded());
            map.put("startTime", instance.getStartTime());

            customTaskList.add(map);
        }
        return customTaskList;
    }

}

