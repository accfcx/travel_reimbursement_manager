package com.test.recipe.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.recipe.common.ResponseResult;
import com.test.recipe.dto.RecipeRequest;
import com.test.recipe.dto.TaskDto;
import com.test.recipe.dto.VariableDto;
import com.test.recipe.enums.FeeType;
import com.test.recipe.enums.RecipeStatus;
import com.test.recipe.enums.RecipeType;
import com.test.recipe.mapper.*;
import com.test.recipe.model.*;
import com.test.recipe.service.Invoice2;
import com.test.recipe.service.OfdInvoiceExtractor;
import com.test.recipe.service.PdfInvoiceExtractor;
import org.activiti.api.runtime.model.impl.ProcessInstanceImpl;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;

import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author accfcx
 **/
@RestController
@RequestMapping("/receipt")
public class RecipeController {
    @Autowired
    private RuntimeService runtimeService;

    @Resource
    TaskRuntime taskRuntime;

    @Resource
    HistoryService historyService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private TaskService taskService;

    @Resource
    private RecipeMapper recipeMapper;

    @Resource
    private TravelApplyMapper travelApplyMapper;

    @Resource
    private OvertimeApplyMapper overtimeApplyMapper;

    @Resource
    private TravelReimbursementMapper travelReimbursementMapper;

    @Resource
    private DailyReimbursementMapper dailyReimbursementMapper;

    @Resource
    private ProcessDefMapper processDefMapper;

    @Resource
    private PersonMapper personMapper;

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    private Gson gson;


    @Resource
    FeeItemMapper feeItemMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 我提交的单据列表
    @PostMapping("/list")
    public Object recipeListByMe(@RequestBody RecipeRequest request) {
        Long uid = request.getUid();
        Recipe query = new Recipe();
        query.setUid(uid);
        List<Recipe> recipes = recipeMapper.selectAllRecipes(query);
        recipes.forEach(item -> {
            item.setDepartmentName(departmentMapper.findById(item.getDepartmentId()).getName());
        });

        return recipes;
    }

    @PostMapping("/listAll")
    public Object recipeListByAdmin(@RequestBody RecipeRequest request) {
        Recipe query = new Recipe();
        if (Objects.nonNull(request.getUid())) {
            query.setUid(request.getUid());
        }
        if (StringUtils.isNotEmpty(request.getReason())) {
            query.setReason(request.getReason());
        }
        if (StringUtils.isNotEmpty(request.getNo())) {
            query.setNo(request.getNo());
        }
        if (Objects.nonNull(request.getCreateTimeStart()) && Objects.nonNull(request.getCreateTimeEnd())) {
            query.setCreateTimeStart(request.getCreateTimeStart());
            query.setCreateTimeEnd(request.getCreateTimeEnd());
        }
        if (StringUtils.isNotEmpty(request.getQueryRecipeType())) {
            if (request.getQueryRecipeType().contains("SQD")) {
                List<String> recipeTypeList = new ArrayList<>();
                recipeTypeList.add(RecipeType.TRAVEL_APPLY.getCode());
                recipeTypeList.add(RecipeType.OVERTIME_APPLY.getCode());
                query.setRecipeTypeList(recipeTypeList);
            } else if (request.getQueryRecipeType().contains("BXD")) {
                List<String> recipeTypeList = new ArrayList<>();
                recipeTypeList.add(RecipeType.DAILY_REIMBURSEMENT.getCode());
                recipeTypeList.add(RecipeType.TRAVEL_REIMBURSEMENT.getCode());
                query.setRecipeTypeList(recipeTypeList);
            }
        }
        return recipeMapper.selectAllRecipes(query);
    }

    @PostMapping("/edit")
    public void updateRecipe(@RequestBody RecipeRequest request) {
        String recipeType = request.getRecipeType();
        Recipe result = recipeMapper.selectRecipe(request.getId());
        Recipe recipe = new Recipe();
        recipe.setId(request.getId());
        recipe.setNo(result.getNo());
        recipe.setRecipeType(result.getRecipeType());
        recipe.setAmount(request.getAmount());
        recipe.setUid(result.getUid());
        recipe.setDepartmentId(result.getDepartmentId());
        recipe.setReason(request.getReason());
        recipe.setRecipeStatus(request.getRecipeStatus());
        recipe.setDenyDetail(request.getDenyDetail());
        recipe.setProcessInstanceId(request.getProcessInstanceId());

        recipe.setCreateTime(result.getCreateTime());
        recipe.setUpdateTime(result.getUpdateTime());

        recipeMapper.updateRecipe(recipe);

        if (RecipeType.TRAVEL_APPLY.getCode().equals(recipeType)) {
            TravelApply apply = request.getTravelApply();
            travelApplyMapper.updateTravelApply(apply);
        } else if (RecipeType.OVERTIME_APPLY.getCode().equals(recipeType)) {
            OvertimeApply overtimeApply = request.getOvertimeApply();
            overtimeApplyMapper.updateOvertimeApply(overtimeApply);
        } else if (RecipeType.TRAVEL_REIMBURSEMENT.getCode().equals(recipeType)) {
            TravelReimbursement travelReimbursement = request.getTravelReimbursement();
            travelReimbursementMapper.updateTravelReimbursement(travelReimbursement);
        } else if (RecipeType.DAILY_REIMBURSEMENT.getCode().equals(recipeType)) {
            DailyReimbursement dailyReimbursement = request.getDailyReimbursement();
            dailyReimbursementMapper.updateDailyReimbursement(dailyReimbursement);
        }
    }

    @PostMapping("/query")
    public RecipeRequest query(@RequestBody RecipeRequest request) {

        Recipe recipe = recipeMapper.selectRecipe(request.getId());

        String recipeStrValue = gson.toJson(recipe);

        RecipeRequest result = gson.fromJson(recipeStrValue, RecipeRequest.class);

        String recipeType = recipe.getRecipeType();

        if (RecipeType.TRAVEL_APPLY.getCode().equals(recipeType)) {
            result.setTravelApply(travelApplyMapper.findByRecipeId(recipe.getId()));
        } else if (RecipeType.OVERTIME_APPLY.getCode().equals(recipeType)) {
            result.setOvertimeApply(overtimeApplyMapper.findByRecipeId(recipe.getId()));
        } else if (RecipeType.TRAVEL_REIMBURSEMENT.getCode().equals(recipeType)) {
            result.setTravelReimbursement(travelReimbursementMapper.findByRecipeId(recipe.getId()));
        } else if (RecipeType.DAILY_REIMBURSEMENT.getCode().equals(recipeType)) {
            result.setDailyReimbursement(dailyReimbursementMapper.findByRecipeId(recipe.getId()));
        }

        FeeItem feeItem = feeItemMapper.findByReceiptId(recipe.getId());
        result.setFeeItem(feeItem);

        return result;
    }

    @PostMapping("/delete")
    public String delete(@RequestBody RecipeRequest request) {
        // 新增 isDeleted字段表示逻辑删除

        Recipe recipe = recipeMapper.selectRecipe(request.getId());
        if (RecipeStatus.INIT.getCode().equals(recipe.getRecipeStatus())) {
            recipeMapper.deleteRecipe(request.getId());
            String recipeType = request.getRecipeType();

            if (RecipeType.TRAVEL_APPLY.getCode().equals(recipeType)) {
                travelApplyMapper.deleteTravelApply(request.getId());
            } else if (RecipeType.OVERTIME_APPLY.getCode().equals(recipeType)) {
                overtimeApplyMapper.deleteOvertimeApply(request.getId());
            } else if (RecipeType.TRAVEL_REIMBURSEMENT.getCode().equals(recipeType)) {
                travelReimbursementMapper.deleteTravelReimbursement(request.getId());
            } else if (RecipeType.DAILY_REIMBURSEMENT.getCode().equals(recipeType)) {
                dailyReimbursementMapper.deleteDailyReimbursement(request.getId());
            }
            return "suc";
        }
        return "只允许删除编辑中的单据";
    }

    @PostMapping("/add")
    public Long addRecipe(@RequestBody RecipeRequest request) {
        FeeItem feeItem = request.getFeeItem();

        Recipe recipe = new Recipe();
        recipe.setRecipeType(request.getRecipeType());
        recipe.setAmount(request.getAmount());
        recipe.setUid(request.getUid());
        recipe.setDepartmentId(request.getDepartmentId());
        recipe.setReason(request.getReason());
        recipe.setRecipeStatus(RecipeStatus.INIT.getCode());
        recipe.setDenyDetail("");
        recipe.setProcessInstanceId("");

        recipeMapper.insertRecipe(recipe);

        String recipeType = request.getRecipeType();

        if (RecipeType.TRAVEL_APPLY.getCode().equals(recipeType)) {
            TravelApply apply = request.getTravelApply();
            apply.setRecipeId(recipe.getId());
            travelApplyMapper.insertTravelApply(apply);
        } else if (RecipeType.OVERTIME_APPLY.getCode().equals(recipeType)) {
            OvertimeApply overtimeApply = request.getOvertimeApply();
            overtimeApply.setRecipeId(recipe.getId());
            overtimeApplyMapper.insertOvertimeApply(overtimeApply);
        } else if (RecipeType.TRAVEL_REIMBURSEMENT.getCode().equals(recipeType)) {
            TravelReimbursement travelReimbursement = request.getTravelReimbursement();
            travelReimbursement.setRecipeId(recipe.getId());
            travelReimbursementMapper.insertTravelReimbursement(travelReimbursement);
        } else if (RecipeType.DAILY_REIMBURSEMENT.getCode().equals(recipeType)) {
            DailyReimbursement dailyReimbursement = request.getDailyReimbursement();
            dailyReimbursement.setRecipeId(recipe.getId());
            dailyReimbursementMapper.insertDailyReimbursement(dailyReimbursement);
        }

        if (feeItem != null && (RecipeType.DAILY_REIMBURSEMENT.getCode().equals(recipeType)) || RecipeType.TRAVEL_REIMBURSEMENT.getCode().equals(recipeType)) {
            feeItem.setRecipeId(recipe.getId());
            feeItem.setCreateTime(new Date());
            feeItem.setUpdateTime(new Date());
            if (feeItem.getPurpose() == null) {
                feeItem.setPurpose(recipe.getReason());
            }

            if (FeeType.Transportation_Fee.getCode().equals(feeItem.getFeeType())) {
                // 交通 用途、出发和目的地
                String purpose = feeItem.getPurpose();
                String startCity = feeItem.getStartCity();
                String endCity = feeItem.getEndCity();
                if (StringUtils.isNotEmpty(purpose) && StringUtils.isNotEmpty(startCity) && StringUtils.isNotEmpty(endCity)) {
                    feeItemMapper.insert(feeItem);
                }
            } else if (FeeType.Team_Building.getCode().equals(feeItem.getFeeType())) {
                // 部门团建 月份和人数
                String month = feeItem.getMonth();
                Long peopleCount = feeItem.getPeopleCount();
                if (StringUtils.isNotEmpty(month) && peopleCount != null) {
                    feeItemMapper.insert(feeItem);
                }
            } else if (FeeType.Travel_Reimbursement.getCode().equals(feeItem.getFeeType())) {
                // 差旅报销 出发和目的地
                String startCity = feeItem.getStartCity();
                String endCity = feeItem.getEndCity();
                if (StringUtils.isNotEmpty(startCity) && StringUtils.isNotEmpty(endCity)) {
                    feeItemMapper.insert(feeItem);
                }
            }
        }

        String formattedDate = LocalDate.now().format(formatter);
        recipe.setNo("B" + formattedDate + recipe.getId());
        if (feeItem != null) {
            recipe.setAmount(feeItem.getAmount());
        }
        recipeMapper.updateRecipe(recipe);


        return recipe.getId();
    }

    @GetMapping("/approval/track/{id}")
    public Map<String, Object> track(@PathVariable Long id) {
        Recipe recipe = recipeMapper.selectRecipe(id);
        String processInstanceId = recipe.getProcessInstanceId();
        String processDefinitionId = processDefMapper.findByType(recipe.getRecipeType()).getProcessDefinitionId();

        // 获取流程定义的task列表
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<TaskDto> taskDtoList = new ArrayList<>();
        if (bpmnModel != null) {
            Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
            taskDtoList = flowElements.stream().filter(flowElement -> flowElement instanceof UserTask).map(flowElement -> {
                String name = flowElement.getName();
                TaskDto dto = new TaskDto();
                dto.setActivityId(flowElement.getId());
                dto.setTaskName(name);
                return dto;
            }).collect(Collectors.toList());
        }

        // 获取流程实例执行
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();
        boolean instanceFinished = false;
        String businessKey = null;
        String name = null;
        Date startTime = null;
        Date endTime = null;
        if (instance == null) {
            instanceFinished = true;
            HistoricProcessInstance instancev2 = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();
            businessKey = instancev2.getBusinessKey();
            name = instancev2.getProcessDefinitionName();
            startTime = instancev2.getStartTime();
            endTime = instancev2.getEndTime();
        } else {
            businessKey = instance.getBusinessKey();
            name = instance.getProcessDefinitionName();
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
                    String prefix = task.getTaskDefinitionKey() + "_SPLIT_";
                    formFieldMap.put(variableName.substring(prefix.length()), value);
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

        map.put("taskList", taskDtoList);
        map.put("waitingTaskList", waitingTaskList);
        map.put("finishedTaskList", finishedIdList);
        return map;

//        Map<String, List<?>> taskDetails = new HashMap<>();
//
//        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
//        List<Task> allTasks = tasks.getContent().stream()
//                .filter(task -> task.getProcessInstanceId().equals(processInstanceId))
//                .collect(Collectors.toList());
//        taskDetails.put("allTasks", allTasks);
//
//        List<HistoricTaskInstance> completedTasks = historyService.createHistoricTaskInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .finished()
//                .list();
//        taskDetails.put("completedTasks", completedTasks);
//
//        List<HistoricTaskInstance> rejectedTasks = historyService.createHistoricTaskInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .taskVariableValueEquals("rejected", true)
//                .list();
//        taskDetails.put("rejectedTasks", rejectedTasks);
//
//        List<Task> readyTasks = tasks.getContent().stream()
//                .filter(task -> task.getProcessInstanceId().equals(processInstanceId) && task.getStatus() == Task.TaskStatus.CREATED)
//                .collect(Collectors.toList());
//        taskDetails.put("readyTasks", readyTasks);
//
//        return taskDetails;
    }


    // 提交单据审批
    @PostMapping("/submit")
    public Object bxdSubmitApprove(@RequestBody RecipeRequest request) {
        // 启动流程实例 - 根据业务类型，选用不同的流程
        Recipe recipe = recipeMapper.selectRecipe(request.getId());

        // 只有未提交才可以提交审批
        if (RecipeStatus.INIT.getCode().equals(recipe.getRecipeStatus())) {
            String businessKey = recipe.getRecipeType() + "##" + recipe.getId();
            ProcessDef processDef = processDefMapper.findByType(recipe.getRecipeType());

            // 变量赋值
            String publishVariables = processDef.getPublishVariables();
            List<VariableDto> variableDtoList = gson.fromJson(publishVariables, new TypeToken<List<VariableDto>>() {
            }.getType());

            Person submitter = personMapper.findById(recipe.getUid());
            Map<String, Object> variables = new HashMap<>();
            variableDtoList.forEach(variableDto -> {
                if (variableDto.getVariable().equals("submitter")) {
                    variables.put("submitter", submitter.getUsername());
                } else if (variableDto.getVariable().equals("leader")) {
                    Department department = departmentMapper.findById(submitter.getDepartmentId());
                    Person leader = personMapper.findById(department.getLeaderId());

                    // 如果单据提交人 和所在部门的leader 是同一人
                    variables.put("leader", leader.getUsername());
                }
            });

            ExecutionEntityImpl processInstance = (ExecutionEntityImpl) runtimeService.createProcessInstanceBuilder().processDefinitionId(processDef.getProcessDefinitionId()).businessKey(businessKey).variables(variables).start();

            System.out.println(processInstance);

            recipe.setProcessInstanceId(processInstance.getId());
            recipe.setRecipeStatus(RecipeStatus.APPROVAL.getCode());
            recipe.setUpdateTime(new Date());
            recipeMapper.updateRecipe(recipe);

            // 获取运行中task, 如果是自己就完成
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(recipe.getProcessInstanceId()).taskAssignee(submitter.getUsername()).list();
            List<TaskDto> list = tasks.stream().map(TaskDto::new).collect(Collectors.toList());
            if (list != null && list.size() > 0) {
                taskService.complete(list.get(0).getTaskInstanceId());
            }

            return "提交审批成功";

        } else {
            return "单据已提交审批，禁止重复提交";
        }
    }
}
