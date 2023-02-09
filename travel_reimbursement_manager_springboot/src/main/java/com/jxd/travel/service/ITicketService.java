package com.jxd.travel.service;

import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.dto.TicketResult;
import com.jxd.travel.model.Ticket;
import com.jxd.travel.model.TravelInfo;
import com.jxd.travel.vo.SearchByTime;

import java.util.List;

public interface ITicketService {
    /**
     * @Author LiJian
     * @Description 根据时间查询车票信息
     * @Param Map - 查询条件
     * @Return 返回响应信息
     * @Date 2023/1/30 14:01
     * @Since version-1.0
     */
    ResponseResult<List<TicketResult>> getTicketsByPage(SearchByTime queryMap);
    
    /**
     * @Author LiJian
     * @Description 获取所有车票信息
     * @Param Integer - 车票所属员工编号
     * @Return 返回响应结果
     * @Date 2023/2/1 16:38
     * @Since version-1.0
     */
    ResponseResult<List<TicketResult>> getTickets(Integer empno);

    /**
     * @Author LiJian
     * @Description 删除车票信息
     * @Param List - 要删除的车票编号
     * @Return 返回是否删除成功
     * @Date 2023/1/30 19:16
     * @Since version-1.0
     */
    boolean delTickets(List<Integer> tids);

    /**
     * @Author LiJian
     * @Description 添加车票信息
     * @Param Ticket - 要添加的车票
     * @Return 返回是否添加成功
     * @Date 2023/1/31 21:22
     * @Since version-1.0
     */
    boolean addTicket(Ticket ticket);
}
