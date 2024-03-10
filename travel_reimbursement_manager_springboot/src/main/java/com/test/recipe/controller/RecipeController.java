package com.test.recipe.controller;

import com.test.recipe.dto.RecipeRequest;
import com.test.recipe.enums.RecipeType;
import com.test.recipe.mapper.*;
import com.test.recipe.model.*;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author accfcx
 **/
@RestController
@RequestMapping("/receipt")
public class RecipeController {
    @Autowired
    private RuntimeService runtimeService;

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

    // 单据列表页面， 权限控制 TODO

    // 差率申请单 travelApply
    // 加班申请单 overtimeApply

    // 差旅报销单 travelReimbursement
    // 日常报销单 dailyReimbursement
    // TODO 需要提供审批字段，用于展示审批进度

    // 我提交的单据列表
    @GetMapping("/list")
    public Object recipeListByMe(@RequestBody RecipeRequest request) {
        Long uid = request.getUid();
        Recipe query = new Recipe();
        query.setUid(uid);
        return recipeMapper.selectAllRecipes(query);
    }

    @PostMapping("/update")
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

    @GetMapping("/query")
    public RecipeRequest query(@RequestBody RecipeRequest request) {
        return null;
    }


    // 提交单据审批
    @PostMapping("/submit")
    public Object bxdSubmitApprove(@RequestBody RecipeRequest receipt) {
        // 启动流程实例 - 根据业务类型，选用不同的流程
        //             .businessKey(businessKey)

//        ProcessInstanceImpl processInstance = (ProcessInstanceImpl) runtimeService
//                .createProcessInstanceBuilder()
//                .processDefinitionKey(processDefinitionKey)
//                .businessKey(businessKey)
//                .start();

        // 单据id 单据类型 用户姓名
        return "suc";
    }
}
