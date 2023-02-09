package com.jxd.travel.controller;

import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.model.User;
import com.jxd.travel.service.IEmpService;
import com.jxd.travel.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/5
 * @Version 1.0
 */
@RestController
public class UserController {
    @Autowired
    private IEmpService empService;
    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public ResponseResult<String> login(@RequestBody User user) {
        return empService.login(user);
    }

    @RequestMapping("/resetPwd/{uid}")
    public String resetPwd(@PathVariable("uid") Integer empno) {
        if (userService.resetPwd(empno)) {
            return "success";
        } else {
            return "error";
        }
    }
}
