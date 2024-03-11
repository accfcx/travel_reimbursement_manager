package com.test.recipe.controller;

import com.test.recipe.mapper.PermissionMapper;
import com.test.recipe.mapper.PersonMapper;
import com.test.recipe.model.Permission;
import com.test.recipe.model.Person;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/permission")
public class PermissonController {

    @Resource
    PersonMapper personMapper;

    @Resource
    PermissionMapper permissionMapper;


    @PostMapping("/isAdmin/{uid}")
    public Boolean isAdmin(@PathVariable Long uid) {
        Person person = personMapper.findById(uid);

        Permission permission = permissionMapper.findByName(person.getUsername());

        if ("admin".equals(permission.getRole())) {
            return true;
        }
        return false;
    }
}
