package com.test.recipe.controller;


import com.test.recipe.mapper.FeeItemMapper;
import com.test.recipe.mapper.RecipeMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    @Resource
    FeeItemMapper feeItemMapper;

    @Resource
    RecipeMapper recipeMapper;

    // 单据表按照审批状态聚合、单据表按照月份聚合、费用项按照类型聚合)
    @PostMapping("/sum/")
    public Map<String, Object> dataMap() {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> aggStatus = recipeMapper.aggByApprovalStatus();
        Map<String, Object> aggMonth = feeItemMapper.aggByMonth();
        Map<String, Object> aggType = feeItemMapper.aggByFeeType();

        result.put("aggStatus", aggStatus);
        result.put("aggMonth", aggMonth);
        result.put("aggType", aggType);

        return result;
    }

}
