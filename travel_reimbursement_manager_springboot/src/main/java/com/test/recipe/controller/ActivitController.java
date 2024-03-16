package com.test.recipe.controller;

import com.test.recipe.dto.TaskDto;
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
import org.activiti.engine.history.HistoricProcessInstance;
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
import org.activiti.engine.history.HistoricVariableInstance;

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


    @GetMapping("/definitionByKey/{processDefinitionKey}")
    public String getXMLByProcessDefinition(@PathVariable String processDefinitionKey) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        String processDefinitionId = processDefinition.getId();
        InputStream xmlStream = repositoryService.getProcessModel(processDefinitionId);
        String xml = new Scanner(xmlStream, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
        return xml;
    }

    @GetMapping("/definitionById/{processDefinitionId}")
    public String getXMLByProcessDefinitionId(@PathVariable String processDefinitionId) {
        InputStream xmlStream = repositoryService.getProcessModel(processDefinitionId);
        String xml = new Scanner(xmlStream, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
        return xml;
    }

    // 获取流程定义的task列表
    @GetMapping("/definitionById/taskDefinitions/{processDefinitionId}")
    public List<TaskDto> getTaskDefinitions(@PathVariable String processDefinitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        if (bpmnModel != null) {
            Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
            return flowElements.stream().filter(flowElement -> flowElement instanceof UserTask).map(flowElement -> {
                String id = flowElement.getId();
                String name = flowElement.getName();
                TaskDto dto = new TaskDto();
                dto.setActivityId(id);
                dto.setTaskName(name);
                return dto;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    // 获取指定流程定义的所有运行中的task
    @GetMapping("/getRunningTasksById/{processDefinitionId}")
    public List<TaskDto> getTasks(@PathVariable String processDefinitionId) {
        List<Task> tasks = taskService.createTaskQuery().processDefinitionId(processDefinitionId).list();
        return tasks.stream().map(TaskDto::new).collect(Collectors.toList());
    }

    // 获取流程实例的各种task运行状态
    @GetMapping("/instanceTaskList/{processInstanceId}")
    public Map<String, Object> getProcessInstance(@PathVariable String processInstanceId) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();

        boolean instanceFinished = false;

        String businessKey = null;
        String name = null;
        String processDefinitionId = null;
        Date startTime = null;
        Date endTime = null;
        if (instance == null) {
            instanceFinished = true;

            HistoricProcessInstance instancev2 = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();
            businessKey = instancev2.getBusinessKey();
            name = instancev2.getProcessDefinitionName();
            processDefinitionId = instancev2.getProcessDefinitionId();
            startTime = instancev2.getStartTime();
            endTime = instancev2.getEndTime();
        } else {
            businessKey = instance.getBusinessKey();
            name = instance.getProcessDefinitionName();
            processDefinitionId = instance.getProcessDefinitionId();
            startTime = instance.getStartTime();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("instanceId", processInstanceId);
        map.put("businessKey", businessKey);
        map.put("name", name);
        map.put("processDefinitionId", processDefinitionId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        // 获取当前活动节点的 ID
        List<String> waitingTaskList = new ArrayList<>();
        if (!instanceFinished) {
            waitingTaskList = runtimeService.getActiveActivityIds(processInstanceId);
        }

        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        // 获取已完成节点的id - 生成的task实例id
        List<HistoricTaskInstance> finishedTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished().list().stream().collect(Collectors.toList());
        // 转换为xml 定义的id
        List<Map<String, Object>> finishedIdList = finishedTaskList.stream().map(task -> {
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
            String xmlId = historicTaskInstance.getTaskDefinitionKey();

            Map<String, Object> formFieldMap = new HashMap<>();
            variableInstances.forEach(item -> {
                String variableName = item.getVariableName();
                Object value = item.getValue();
                if (variableName.startsWith(task.getTaskDefinitionKey())) {
                    formFieldMap.put(variableName.substring(task.getTaskDefinitionKey().length() + 1), value);
                } else {
                    if (!variableName.contains("_SPLIT_")) {
                        formFieldMap.put(variableName, value);
                    }
                }
            });

            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("taskName", task.getName());
            infoMap.put("taskId", task.getId());
            infoMap.put("xmlId", xmlId);
            infoMap.put("assignee", task.getAssignee());
            infoMap.put("formFieldMap", formFieldMap);

            return infoMap;
        }).collect(Collectors.toList());

        map.put("waitingTaskList", waitingTaskList);
        map.put("finishedTaskList", finishedIdList);

        return map;
    }

    @PostMapping("/completeTask/{taskId}")
    public void completeOneTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        taskService.complete(taskId, variables);
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

