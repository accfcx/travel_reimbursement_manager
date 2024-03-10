package com.jxd.travel;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxd.travel.controller.EmpController;
import com.jxd.travel.dao.IEmpDao;
import com.jxd.travel.dao.ITicketDao;
import com.jxd.travel.dao.ITravelInfoDao;
import com.jxd.travel.dao.IUserDao;
import com.jxd.travel.model.Emp;
import com.jxd.travel.model.Ticket;
import com.jxd.travel.service.IEmpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ApplicationTest
 * @Author LiJian
 * @Date 2023/1/28
 * @Version 1.0
 */
@SpringBootTest(classes = TravelApplication.class)
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    private IEmpService empService;
    @Autowired
    private IEmpDao empDao;
    @Autowired
    private EmpController empController;
    @Autowired
    private ITicketDao ticketDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private ITravelInfoDao travelInfoDao;

    @Test
    public void test() {
        Emp emp1 = new Emp(30007, "小白", "男", "13500324652");
        empService.addEmp(emp1);
    }

    @Test
    public void testPage() {
        IPage<Emp> page = new Page<>();
        // 设置每页条数
        page.setSize(2);
        // 设置查询第几页
        page.setCurrent(1);
        IPage<Emp> page1 = empDao.selectPage(page, null);
        System.out.println(page == page1);
    }

    @Test
    public void testPageByEname() {
        String name = "张";
        IPage<Emp> page = new Page<>();
        // 设置每页条数
        page.setSize(2);
        // 设置查询第几页
        page.setCurrent(1);
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("ename", "张三");
        queryMap.put("page", "1");
        queryMap.put("limit", "2");
        empService.getEmps(queryMap);
    }

    @Test
    public void testSelectMaxEmpno() {
        System.out.println(empDao.selectMaxEmpno());
    }

    // @Test
    // public void testGetTicket() {
    //     SearchTicket ticket = new SearchTicket(1, 2, "2022-7-11", "2022-7-12");
    //     ResponseResult<List<TicketResult>> result =
    //             empController.getTickets(ticket);
    //     System.out.println(result.getData());
    //     System.out.println(result.getCount());
    // }


    @Test
    public void testAddTicket() {
        Ticket ticket = new Ticket();
        ticket.setTravelTypeId(1);
    }

    @Test
    public void testResetPwd() {
        System.out.println(userDao.resetPwd(30004));
    }

    @Test
    public void testSelectMax() {
        System.out.println(travelInfoDao.selectMaxTravelId());
    }

    @Test
    public void testStrem(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(5);
        list.add(5);
        list.add(6);
        List<Integer> filteredList =
                list.stream().distinct().collect(Collectors.<Integer>toList());
        System.out.println(1);
    }
}
