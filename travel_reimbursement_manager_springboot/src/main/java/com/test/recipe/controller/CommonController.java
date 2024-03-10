package com.test.recipe.controller;

import com.test.recipe.enums.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/dict")
    public Map<String, Object> dict() {
        Map<String, Object> dict = new HashMap<>();
        // 费用项类型
        Map<String, Object> feeMap = new HashMap<>();
        for (FeeType value : FeeType.values()) {
            feeMap.put("code", value.getCode());
            feeMap.put("desc", value.getDesc());
        }
        dict.put("feeType", feeMap);

        // 单据类型
        Map<String, Object> recipeTypeMap = new HashMap<>();
        for (RecipeType value : RecipeType.values()) {
            feeMap.put("code", value.getCode());
            feeMap.put("desc", value.getDesc());
        }
        dict.put("recipeType", recipeTypeMap);

        // 单据状态
        Map<String, Object> recipeStatusMap = new HashMap<>();
        for (RecipeStatus value : RecipeStatus.values()) {
            feeMap.put("code", value.getCode());
            feeMap.put("desc", value.getDesc());
        }
        dict.put("recipeStatus", recipeStatusMap);

        // 差旅目的
        Map<String, Object> travelTargetMap = new HashMap<>();
        for (TravelTarget value : TravelTarget.values()) {
            feeMap.put("code", value.getCode());
            feeMap.put("desc", value.getDesc());
        }
        dict.put("travelTarget", travelTargetMap);

        // 差旅申请单 - 申请类型
        Map<String, Object> applyTypeMap = new HashMap<>();
        for (ApplyType value : ApplyType.values()) {
            feeMap.put("code", value.getCode());
            feeMap.put("desc", value.getDesc());
        }
        dict.put("applyType", applyTypeMap);

        return dict;

    }

}
