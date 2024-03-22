package com.test.recipe.controller;


import com.google.gson.Gson;
import com.test.recipe.enums.RecipeStatus;
import com.test.recipe.mapper.FeeItemMapper;
import com.test.recipe.mapper.RecipeMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

        List<Map<String, Object>> aggStatusV2 = new ArrayList<>();
        aggStatus.forEach(item -> {
            if (String.valueOf(item.get("recipe_status")).equals(RecipeStatus.FINISHED.getCode()) || String.valueOf(item.get("recipe_status")).equals(RecipeStatus.REJECTED.getCode())) {
            } else {
                aggStatusV2.add(item);
            }
        });

        List<Map<String, Object>> aggMonthMapList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> defaultEntry = new HashMap<>();
            defaultEntry.put("month", i);
            defaultEntry.put("total_amount", BigDecimal.ZERO);
            aggMonthMapList.add(defaultEntry);
        }

        aggMonth.forEach(item -> {
            int month = (int) item.get("month");
            BigDecimal amount = (BigDecimal) item.get("total_amount");
            for (Map<String, Object> entry : aggMonthMapList) {
                if ((int) entry.get("month") == month) {
                    entry.put("total_amount", amount);
                    break;
                }
            }
        });

        result.put("aggStatus", aggStatusV2);
        result.put("aggMonth", aggMonthMapList);
        result.put("aggType", aggType);



        return result;
    }

}
