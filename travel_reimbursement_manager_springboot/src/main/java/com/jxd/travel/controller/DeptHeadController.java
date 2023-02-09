package com.jxd.travel.controller;

import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.dto.ReimbursementResult;
import com.jxd.travel.service.ITravelInfoService;
import com.jxd.travel.vo.PassForm;
import com.jxd.travel.vo.RejectForm;
import com.jxd.travel.vo.SearchByStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DeptHeadController
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/4
 * @Version 1.0
 */
@RestController
public class DeptHeadController {
    @Autowired
    ITravelInfoService travelInfoService;

    @RequestMapping("/getReimbursementList")
    public ResponseResult<List<ReimbursementResult>> getReimbursementList(@RequestBody SearchByStatus queryMap) {
        return travelInfoService.getReimbursementList(queryMap);
    }

    @RequestMapping("/passReimbursement")
    public String passReimbursement(@RequestBody PassForm passForm){
        if (travelInfoService.passReimbursement(passForm)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/rejectReimbursement")
    public String rejectReimbursement(@RequestBody RejectForm rejectForm){
        if(travelInfoService.rejectReimbursement(rejectForm)){
            return "success";
        } else {
            return "error";
        }
    }
}
