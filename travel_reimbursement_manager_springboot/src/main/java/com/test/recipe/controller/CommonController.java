package com.test.recipe.controller;

import com.test.recipe.common.ResponseResult;
import com.test.recipe.dto.PersonDto;
import com.test.recipe.enums.*;
import com.test.recipe.mapper.DepartmentMapper;
import com.test.recipe.mapper.PermissionMapper;
import com.test.recipe.mapper.PersonMapper;
import com.test.recipe.model.Department;
import com.test.recipe.model.Permission;
import com.test.recipe.model.Person;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    PersonMapper personMapper;

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    PermissionMapper permissionMapper;

    @GetMapping("/dict")
    public Map<String, Object> dict() {
        Map<String, Object> dict = new HashMap<>();

        // 单据类型
        List<Map<String, Object>> recipeTypeMapList = new ArrayList<>();

        for (RecipeType value : RecipeType.values()) {
            Map<String, Object> recipeTypeMap = new HashMap<>();

            recipeTypeMap.put("code", value.getCode());
            recipeTypeMap.put("desc", value.getDesc());

            recipeTypeMapList.add(recipeTypeMap);
        }
        dict.put("recipeType", recipeTypeMapList);


        // 费用项类型
        Map<String, Object> feeMap = new HashMap<>();
        for (FeeType value : FeeType.values()) {
            feeMap.put("code", value.getCode());
            feeMap.put("desc", value.getDesc());
        }
        dict.put("feeType", feeMap);


        // 单据状态
        List<Map<String, Object>> recipeStatusMapList = new ArrayList<>();
        for (RecipeStatus value : RecipeStatus.values()) {
            Map<String, Object> recipeStatusMap = new HashMap<>();
            recipeStatusMap.put("code", value.getCode());
            recipeStatusMap.put("desc", value.getDesc());

            recipeStatusMapList.add(recipeStatusMap);
        }
        dict.put("recipeStatus", recipeStatusMapList);

        // 差旅申请单 - 申请类型
        List<Map<String, String>> applyTypeMapList = new ArrayList<>();

        for (TravelApplyType value : TravelApplyType.values()) {
            Map<String, String> applyTypeMap = new HashMap<>();
            applyTypeMap.put("code", value.getCode());
            applyTypeMap.put("desc", value.getDesc());
            applyTypeMapList.add(applyTypeMap);
        }
        dict.put("travelApplyType", applyTypeMapList);

        // 差旅申请目的
        List<Map<String, String>> travelTargetMapList = new ArrayList<>();
        for (TravelTarget value : TravelTarget.values()) {
            Map<String, String> travelTargetMap = new HashMap<>();
            travelTargetMap.put("code", value.getCode());
            travelTargetMap.put("desc", value.getDesc());
            travelTargetMapList.add(travelTargetMap);
        }
        dict.put("travelTarget", travelTargetMapList);

        return dict;
    }


    @PostMapping("/login")
    public ResponseResult<PersonDto> login(@RequestBody Person person) {

        if (person == null || person.getId() == null) {
            return new ResponseResult<>(100, "参数检验失败");
        }

        PersonDto result = new PersonDto();

        Person resultPerson = personMapper.findById(person.getId());

        if (resultPerson.getPassword().equals(person.getPassword())) {
            Department department = departmentMapper.findById(resultPerson.getDepartmentId());
            Permission permission = permissionMapper.findByUid(person.getId());

            result.setPerson(resultPerson);
            result.setDepartment(department);
            result.setPermission(permission);

            return new ResponseResult<>(0, "suc", result);
        }

        return new ResponseResult<>(100, "密码校验失败");

    }

}
