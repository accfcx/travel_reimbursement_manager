package com.jxd.travel.controller;

import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.model.Emp;
import com.jxd.travel.model.Transport;
import com.jxd.travel.service.IEmpService;
import com.jxd.travel.service.ITransportationService;
import com.jxd.travel.vo.SearchByTransportation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ManagerController
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/5
 * @Version 1.0
 */
@RestController
public class ManagerController {
    @Autowired
    private IEmpService empService;
    @Autowired
    private ITransportationService transportationService;

    @RequestMapping("/empList")
    public ResponseResult<List<Emp>> getEmps(@RequestBody Map<String,
            String> queryMap) {
        return empService.getEmps(queryMap);
    }

    @RequestMapping("/addEmp")
    public String addEmp(@RequestBody Emp emp) {
        if (empService.addEmp(emp)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/updateEmp")
    public String updateEmp(@RequestBody Emp emp) {
        if (empService.updateEmp(emp)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/transList")
    public ResponseResult<List<Transport>> getTransList() {
        return transportationService.getAllTransportation();
    }

    @RequestMapping("/transListByPage")
    public ResponseResult<List<Transport>> getTransListByPage(@RequestBody SearchByTransportation queryMap) {
        return transportationService.getTransportationListByPage(queryMap);
    }

    @RequestMapping("/updateTransportation")
    public String updateTransportation(@RequestBody Transport transportation) {
        if (transportationService.updateTransportation(transportation)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/addTransportation")
    public String addTransportation(@RequestBody Transport transportation) {
        if (transportationService.addTransportation(transportation)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/delbatch")
    public String delBath(@RequestBody List<Integer> empnos) {
        if (empService.deleteBatch(empnos)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/delTransportation")
    public String delTransportation(@RequestBody List<Integer> transIds) {
        if(transportationService.deleteTransportation(transIds)){
            return "success";
        } else {
            return "error";
        }
    }
}
