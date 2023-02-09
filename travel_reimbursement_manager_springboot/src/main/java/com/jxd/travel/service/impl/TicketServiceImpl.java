package com.jxd.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.dao.ITicketDao;
import com.jxd.travel.dto.TicketResult;
import com.jxd.travel.model.Ticket;
import com.jxd.travel.service.ITicketService;
import com.jxd.travel.vo.SearchByTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TicketServiceImpl
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/30
 * @Version 1.0
 */
@Service
public class TicketServiceImpl implements ITicketService {
    @Autowired
    private ITicketDao ticketDao;

    @Override
    public ResponseResult<List<TicketResult>> getTicketsByPage(SearchByTime queryMap) {
        // 获取从前台接收到的查询条件
        Integer page = queryMap.getPage();
        Integer limit = queryMap.getLimit();
        String startTime = queryMap.getStartTime();
        String arrivalTime = queryMap.getArrivalTime();
        Integer empno = queryMap.getEmpno();
        ResponseResult<List<TicketResult>> responseResult =
                new ResponseResult<>();
        if (limit != null && page != null) {
            IPage<TicketResult> pages = new Page<>(page,limit);
            ticketDao.selectTicketsByTime(pages, startTime, arrivalTime,empno);
            responseResult.setData(pages.getRecords());
            responseResult.setCode(0);
            responseResult.setCount((int) pages.getTotal());
            return responseResult;
        } else {
            responseResult.setCode(500);
            responseResult.setMsg("参数有误！");
            return responseResult;
        }
    }

    @Override
    public ResponseResult<List<TicketResult>> getTickets(Integer empno) {
        ResponseResult<List<TicketResult>> responseResult =
                new ResponseResult<>();
        List<TicketResult> ticketResultList = ticketDao.selectAllTickets(empno);
        responseResult.setData(ticketResultList);
        responseResult.setCode(0);
        return responseResult;
    }

    @Override
    public boolean delTickets(List<Integer> tids) {
        // 查询已关联差旅的车票
        List<Integer> unAssociatedTids =
                ticketDao.selectTidsWithoutAssociation();

        return ticketDao.delTickets(tids);
    }

    @Override
    public boolean addTicket(Ticket ticket) {
        return ticketDao.addTicket(ticket);
    }
}
