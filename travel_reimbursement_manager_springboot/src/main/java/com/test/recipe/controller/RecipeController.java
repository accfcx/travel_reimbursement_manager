package com.test.recipe.controller;

import com.test.recipe.dto.RecipeRequest;
import com.test.recipe.enums.RecipeStatus;
import com.test.recipe.enums.RecipeType;
import com.test.recipe.mapper.*;
import com.test.recipe.model.*;
import org.activiti.api.runtime.model.impl.ProcessInstanceImpl;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;

import javax.annotation.Resource;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 我提交的单据列表
    @PostMapping("/list")
    public Object recipeListByMe(@RequestBody RecipeRequest request) {
        Long uid = request.getUid();
        Recipe query = new Recipe();
        query.setUid(uid);
        return recipeMapper.selectAllRecipes(query);
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
            if (request.getQueryRecipeType().contains("BXD")) {
                List<String> recipeTypeList = new ArrayList<>();
                recipeTypeList.add(RecipeType.TRAVEL_APPLY.getCode());
                recipeTypeList.add(RecipeType.OVERTIME_APPLY.getCode());
                query.setRecipeTypeList(recipeTypeList);
            } else if (request.getQueryRecipeType().contains("SQD")) {
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

        // 审批状态为拒绝时，展示拒绝原因
        return null;
    }

    @PostMapping("/delete")
    public String delete(@RequestBody RecipeRequest request) {
        // 新增 isDeleted字段表示逻辑删除
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

    @PostMapping("/add")
    public String addRecipe(@RequestBody RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setRecipeType(request.getRecipeType());
        recipe.setAmount(request.getAmount());
        recipe.setUid(request.getUid());
        recipe.setDepartmentId(request.getDepartmentId());
        recipe.setReason(request.getReason());
        recipe.setRecipeStatus(RecipeStatus.INIT.getCode());

        recipeMapper.insertRecipe(recipe);
        String formattedDate = LocalDate.now().format(formatter);

        recipe.setNo("B" + formattedDate + recipe.getId());
        recipeMapper.updateRecipe(recipe);

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

        return "suc";
    }


    @PostMapping("/approval/track")
    public Map<String, List<?>> track() {
        // 根据业务信息/实例信息 获取到流程
        // 展示哪个流程图

        String processInstanceId = "";

        Map<String, List<?>> taskDetails = new HashMap<>();

        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
        List<Task> allTasks = tasks.getContent().stream()
                .filter(task -> task.getProcessInstanceId().equals(processInstanceId))
                .collect(Collectors.toList());
        taskDetails.put("allTasks", allTasks);

        List<HistoricTaskInstance> completedTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();
        taskDetails.put("completedTasks", completedTasks);

        List<HistoricTaskInstance> rejectedTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskVariableValueEquals("rejected", true)
                .list();
        taskDetails.put("rejectedTasks", rejectedTasks);

        List<Task> readyTasks = tasks.getContent().stream()
                .filter(task -> task.getProcessInstanceId().equals(processInstanceId) && task.getStatus() == Task.TaskStatus.CREATED)
                .collect(Collectors.toList());
        taskDetails.put("readyTasks", readyTasks);

        return taskDetails;
    }


    // 提交单据审批
    @PostMapping("/submit")
    public Object bxdSubmitApprove(@RequestBody RecipeRequest request) {
        // 启动流程实例 - 根据业务类型，选用不同的流程
        Recipe recipe = recipeMapper.selectRecipe(request.getId());
        String businessKey = recipe.getRecipeType() + "##" + recipe.getId();

        String recipeType = recipe.getRecipeType();

        ProcessDef processDef = processDefMapper.findByType(recipeType);

        ProcessInstanceImpl processInstance = (ProcessInstanceImpl) runtimeService
                .createProcessInstanceBuilder()
                .processDefinitionKey(processDef.getProcessDefinitionKey())
                .businessKey(businessKey)
                .start();

        return "suc";
    }
}
