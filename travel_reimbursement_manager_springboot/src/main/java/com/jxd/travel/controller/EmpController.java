package com.jxd.travel.controller;

import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.dto.TicketResult;
import com.jxd.travel.model.*;
import com.jxd.travel.service.*;
import com.jxd.travel.vo.AssociateTickets;
import com.jxd.travel.vo.ChangePwd;
import com.jxd.travel.vo.SearchByTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName EmpController
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/28
 * @Version 1.0
 */
@RestController
public class EmpController {
    @Autowired
    private ITicketService ticketService;
    @Autowired
    private ITravelTypeService travelTypeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ITravelInfoService travelInfoService;
    @RequestMapping("/ticketListByPage")
    public ResponseResult<List<TicketResult>> getTicketsByPage(@RequestBody SearchByTime queryMap) {
        return ticketService.getTicketsByPage(queryMap);
    }

    @RequestMapping("/ticketList/{empno}")
    public ResponseResult<List<TicketResult>> getTickets(@PathVariable("empno") Integer empno) {
        return ticketService.getTickets(empno);
    }

    @RequestMapping("/delTickets")
    public String delTickets(@RequestBody List<Integer> tids) {
        if (ticketService.delTickets(tids)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/travelTypeList")
    public ResponseResult<List<TravelType>> getTravelTypeList() {
        return travelTypeService.getTravelTypeList();
    }

    @Value("${file.address}")
    private String address;

    @RequestMapping("/uploadImg")
    public String uploadImg(@RequestParam("image") MultipartFile upload) {
        ApplicationHome home = new ApplicationHome(getClass());
        File fileJar = home.getSource();
        // 要上传到哪里
        String savePath = fileJar.getParent() + address;

        File file = new File(savePath);
        // 如果不存在，或者不是目录 就创建
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        // 获取原文件名
        String oldName = upload.getOriginalFilename();
        // 对原文件名进行处理，添加前缀
        String newName = UUID.randomUUID() + "_" + oldName;
        // 根据保存路径和新名字生成一个文件对象
        File saveFile = new File(savePath, newName);
        try {
            // 将SpringMVC接收到的文件对象转换为普通的文件对象
            // 转换的过程即保存的过程
            upload.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 返回文件保存路径及文件名
        return address + newName;
    }

    @RequestMapping("/addTicket")
    public String addTicket(@RequestBody Ticket ticket) {
        if (ticketService.addTicket(ticket)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/travelInfoListByPage")
    public ResponseResult<List<TravelInfo>> getTravelInfoList(@RequestBody SearchByTime queryMap) {
        return travelInfoService.getTravelInfoList(queryMap);
    }

    @RequestMapping("/getProcess/{empno}")
    public ResponseResult<List<Double>> getProcess(@PathVariable("empno") Integer empno) {
        return travelInfoService.getProcess(empno);
    }

    @RequestMapping("/associateTickets")
    public String associateTickets(@RequestBody AssociateTickets associateTickets) {
        if (travelInfoService.associateTickets(associateTickets)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/getAssociatedTickets/{travelId}")
    public ResponseResult<AssociateTickets> getAssociatedTickets(@PathVariable Integer travelId) {
        return travelInfoService.getAssociateTickets(travelId);
    }

    @RequestMapping("/delTravelInfo")
    public String delTravelInfo(@RequestBody List<Integer> travelIds) {
        if (travelInfoService.delTravelInfo(travelIds)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/submitReimbursement")
    public String submitReimbursement(@RequestBody AssociateTickets associateTickets) {
        if (travelInfoService.submitReimbursement(associateTickets)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/delFile")
    public String delFile(@RequestBody String fileName) {
        String formatFileName = fileName.substring(12, fileName.length() - 1);
        ApplicationHome home = new ApplicationHome(getClass());
        File fileJar = home.getSource();
        String savePath = fileJar.getParent() + address + formatFileName;
        File file = new File(savePath);
        if (file.delete()) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping("/changePwd")
    public String changePwd(@RequestBody ChangePwd pwdForm) {
        return userService.changePwd(pwdForm);
    }
}
