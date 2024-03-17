package com.test.recipe.controller;


import com.google.gson.Gson;
import com.test.recipe.enums.RecipeStatus;
import com.test.recipe.mapper.FeeItemMapper;
import com.test.recipe.mapper.RecipeMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    @Resource
    FeeItemMapper feeItemMapper;

    @Resource
    RecipeMapper recipeMapper;

    // 单据表按照审批状态聚合、单据表按照月份聚合、费用项按照类型聚合)
    @PostMapping("/sum")
    public Map<String, Object> dataMap() {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> aggStatus = recipeMapper.aggByApprovalStatus();
        List<Map<String, Object>> aggMonth = feeItemMapper.aggByMonth();
        List<Map<String, Object>> aggType = feeItemMapper.aggByFeeType();

        Gson gson = new Gson();
        System.out.println(gson.toJson(aggStatus));

        List<Map<String, Object>> aggStatusV2 = new ArrayList<>();
        aggStatus.forEach(item -> {
            if (String.valueOf(item.get("recipe_status")).equals(RecipeStatus.FINISHED.getCode()) || String.valueOf(item.get("recipe_status")).equals(RecipeStatus.REJECTED.getCode())) {
            } else {
                aggStatusV2.add(item);
            }
        });

        result.put("aggStatus", aggStatusV2);
        result.put("aggMonth", aggMonth);
        result.put("aggType", aggType);

        return result;
    }

}
