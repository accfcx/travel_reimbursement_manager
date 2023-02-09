package com.jxd.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.dao.*;
import com.jxd.travel.model.Emp;
import com.jxd.travel.model.User;
import com.jxd.travel.service.IEmpService;
import com.jxd.travel.service.ITravelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName EmpServiceImpl
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/28
 * @Version 1.0
 */
@Service
public class EmpServiceImpl extends ServiceImpl<IEmpDao, Emp> implements IEmpService {
    @Autowired
    private IEmpDao empDao;
    @Autowired
    private IDeptHeadDao deptHeadDao;
    @Autowired
    private IFinancialDao financialDao;
    @Autowired
    private IManagerDao managerDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private ITicketDao ticketDao;
    @Autowired
    private ITravelInfoService travelInfoService;

    @Override
    public ResponseResult<String> login(User user) {
        ResponseResult<String> responseResult = new ResponseResult<>();
        Integer uid = user.getUid();
        String password = userDao.selectPasswordById(uid);
        // 未查询到对应密码或与查询到的密码不一致
        if (password == null || !password.equals(user.getPassword())) {
            responseResult.setMsg("error");
            responseResult.setCode(500);
            return responseResult;
        } else if (uid.toString().startsWith("1")) {
            responseResult.setData(managerDao.selectManagerName(uid));
            responseResult.setMsg("manager");
        } else if (uid.toString().startsWith("2")) {
            responseResult.setData(deptHeadDao.selectDeptHeadName(uid));
            responseResult.setMsg("deptHead");
        } else if (uid.toString().startsWith("3")) {
            responseResult.setData(empDao.selectEmpName(uid));
            responseResult.setMsg("employee");
        } else {
            responseResult.setData(financialDao.selectFinancialName(uid));
            responseResult.setMsg("financial");
        }
        responseResult.setCode(0);
        return responseResult;
    }

    @Override
    public ResponseResult<List<Emp>> getEmps(Map<String, String> queryMap) {
        // 创建统一格式的响应对象
        ResponseResult<List<Emp>> responseResult = new ResponseResult<>();
        // 获取从前台接收到的查询条件
        String ename = queryMap.get("ename");
        String page = queryMap.get("page");
        String limit = queryMap.get("limit");
        // 设置响应体内容
        if (page != null && limit != null) {
            IPage<Emp> pages = new Page(Integer.parseInt(page), Integer.parseInt(limit));
            // 分页查询，并将查询结果放回pages中
            empDao.selectEmps(pages, ename);
            responseResult.setCount((int) pages.getTotal());
            responseResult.setData(pages.getRecords());
            responseResult.setCode(0);
        } else {
            responseResult.setCode(500);
            responseResult.setMsg("分页参数有误！!");
        }
        return responseResult;
    }

    @Override
    @Transactional
    public boolean addEmp(Emp emp) {
        Integer empno = empDao.selectMaxEmpno() + 1;
        emp.setEmpno(empno);
        return empDao.insertEmp(emp) && userDao.insertUser(empno);
    }

    @Override
    public boolean updateEmp(Emp emp) {
        return empDao.updateEmp(emp);
    }

    @Override
    @Transactional
    public boolean deleteBatch(List<Integer> empnos) {
        boolean isSuccess = true;
        // 查出用户对应的车票编号
        List<Integer> tids = ticketDao.selectTidsByEmpnos(empnos);
        if (tids.size() != 0) {
            // 查出车票关联的所有差旅信息编号
            List<Integer> travelIds = ticketDao.selectTravelIdsByTids(tids);
            // 对差旅编号去重
            List<Integer> distinctedTravelIds = travelIds.stream().distinct().collect(Collectors.toList());
            // 删除差旅信息，补助信息同时也被删除
            if (!travelInfoService.delTravelInfo(distinctedTravelIds)) {
                isSuccess = false;
            }
            // 删除车票
            if (!ticketDao.delTickets(tids)) {
                isSuccess = false;
            }
        }
        // 删除员工信息和用户信息
        if (!empDao.deleteEmps(empnos) || !userDao.deleteUsers(empnos)) {
            isSuccess = false;
        }
        return isSuccess;
    }
}
