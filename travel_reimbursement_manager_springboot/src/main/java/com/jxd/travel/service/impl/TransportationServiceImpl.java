package com.jxd.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.dao.ITicketDao;
import com.jxd.travel.dao.ITransportationDao;
import com.jxd.travel.model.Ticket;
import com.jxd.travel.model.Transport;
import com.jxd.travel.service.ITransportationService;
import com.jxd.travel.service.ITravelInfoService;
import com.jxd.travel.vo.SearchByTransportation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName TransportationImpl
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/31
 * @Version 1.0
 */
@Service
public class TransportationServiceImpl implements ITransportationService {
    @Autowired
    private ITransportationDao transportationDao;
    @Autowired
    private ITicketDao ticketDao;
    @Autowired
    private ITravelInfoService travelInfoService;

    @Autowired

    @Override
    public ResponseResult<List<Transport>> getAllTransportation() {
        ResponseResult<List<Transport>> responseResult =
                new ResponseResult<>();
        List<Transport> transportationList =
                transportationDao.selectAllTransportation();
        responseResult.setCode(0);
        responseResult.setData(transportationList);
        return responseResult;
    }

    @Override
    public ResponseResult<List<Transport>> getTransportationListByPage(SearchByTransportation queryMap) {
        // 获取从前台接收到的查询条件
        Integer page = queryMap.getPage();
        Integer limit = queryMap.getLimit();
        String transportation = queryMap.getTransportation();
        ResponseResult<List<Transport>> responseResult =
                new ResponseResult<>();
        if (limit != null && page != null) {
            IPage<Transport> pages = new Page<>(page, limit);
            transportationDao.selectTranportationListByPage(pages, transportation);
            responseResult.setData(pages.getRecords());
            responseResult.setCode(0);
            responseResult.setCount((int) pages.getTotal());
        } else {
            responseResult.setCode(500);
            responseResult.setMsg("参数有误！");
        }
        return responseResult;
    }

    @Override
    public boolean updateTransportation(Transport transportation) {
        return transportationDao.updateTransportation(transportation);
    }

    @Override
    public boolean addTransportation(Transport transportation) {
        return transportationDao.addTransportation(transportation);
    }

    @Override
    @Transactional
    public boolean deleteTransportation(List<Integer> transIds) {
        // 查询使用该交通工具的车票
        List<Ticket> tickets = ticketDao.selectTicketsByTransId(transIds);
        if (tickets.size() != 0) {
            // 获取车票编号
            List<Integer> tids = tickets.stream().map(ticket -> ticket.getTid())
                    .collect(Collectors.toList());
            List<Integer> travelIds = tickets.stream().filter(ticket -> ticket.getTravelId() != null)
                    .map(ticket -> ticket.getTravelId())
                    .distinct()
                    .collect(Collectors.toList());
            // 若车票已关联差旅，删除相应的差旅信息
            if (travelIds.size() != 0) {
                return ticketDao.delTickets(tids) && travelInfoService.delTravelInfo(travelIds) && transportationDao.deleteTransportation(transIds);
            } else {
                // 否则删除车票和交通工具
                return ticketDao.delTickets(tids) && transportationDao.deleteTransportation(transIds);
            }
        } else {
            // 直接删除交通工具
            return transportationDao.deleteTransportation(transIds);
        }
    }
}
