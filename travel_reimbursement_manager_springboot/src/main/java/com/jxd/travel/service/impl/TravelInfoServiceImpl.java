package com.jxd.travel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.dao.ISubsidyDao;
import com.jxd.travel.dao.ITicketDao;
import com.jxd.travel.dao.ITravelInfoDao;
import com.jxd.travel.dto.ReimbursementResult;
import com.jxd.travel.dto.TicketResult;
import com.jxd.travel.dto.TravelInfoResult;
import com.jxd.travel.model.SubsidyInfo;
import com.jxd.travel.model.TravelInfo;
import com.jxd.travel.service.ITravelInfoService;
import com.jxd.travel.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TravelInfoServiceImpl
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/1
 * @Version 1.0
 */
@Service
public class TravelInfoServiceImpl implements ITravelInfoService {
    @Autowired
    private ITravelInfoDao travelInfoDao;
    @Autowired
    private ITicketDao ticketDao;
    @Autowired
    private ISubsidyDao subsidyDao;

    @Override
    public ResponseResult<List<TravelInfo>> getTravelInfoList(SearchByTime queryMap) {
        // 获取从前台接收到的查询条件
        Integer page = queryMap.getPage();
        Integer limit = queryMap.getLimit();
        String reimbursementDate = queryMap.getReimbursementDate();
        Integer empno = queryMap.getEmpno();
        ResponseResult<List<TravelInfo>> responseResult =
                new ResponseResult<>();
        if (limit != null && page != null) {
            IPage<TravelInfo> pages = new Page<>(page, limit);
            travelInfoDao.selectTravelInfos(pages, reimbursementDate, empno);
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
    public ResponseResult<List<Double>> getProcess(Integer empno) {
        ResponseResult<List<Double>> responseResult = new ResponseResult<>();
        List<Double> statusList = travelInfoDao.selectStatus(empno);
        responseResult.setData(statusList);
        responseResult.setCode(0);
        return responseResult;
    }

    /**
     * @Author LiJian
     * @Description 新增差旅信息
     * @Param 差旅信息
     * @Return 返回是否新增成功
     * @Date 2023/2/3 15:29
     * @Since version-1.0
     */
    @Transactional
    public boolean newAssociateTicket(AssociateTickets associateTickets) {
        boolean isSuccess = true;
        Integer travelId = 1;
        if (travelInfoDao.selectMaxTravelId() != null) {
            travelId = travelInfoDao.selectMaxTravelId() + 1;
        }
        List<SubsidyInfo> subsidyInfos = associateTickets.getSubsidyForm();
        List<TicketResult> tickets = associateTickets.getTickets();
        TravelInfo travelInfo = associateTickets.getTravelForm();
        travelInfo.setTravelId(travelId);
        // 新增一条差旅信息
        if (!travelInfoDao.insertTravelInfo(travelInfo)) {
            isSuccess = false;
        }
        // 新增补助信息
        for (SubsidyInfo subsidyInfo : subsidyInfos) {
            subsidyInfo.setTravelId(travelId);
            if (!subsidyDao.insertSubsidyInfo(subsidyInfo)) {
                isSuccess = false;
            }
        }
        // 车票关联差旅信息
        for (TicketResult ticket : tickets) {
            ticket.setTravelId(travelId);
            ticket.setTravelDesc(travelInfo.getTravelDesc());
            if (!ticketDao.updateTravelId(ticket)) {
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    /**
     * @Author LiJian
     * @Description 编辑未提交的差旅信息
     * @Param 差旅信息
     * @Return 返回是否修改成功
     * @Date 2023/2/3 15:29
     * @Since version-1.0
     */
    @Transactional
    public boolean editAssociateTicket(AssociateTickets associateTickets) {
        boolean isSuccess = true;
        List<SubsidyInfo> subsidyInfos = associateTickets.getSubsidyForm();
        List<TicketResult> tickets = associateTickets.getTickets();
        TravelInfo travelInfo = associateTickets.getTravelForm();
        Integer travelId = travelInfo.getTravelId();
        List<Integer> travelIdList = new ArrayList<>();
        travelIdList.add(travelId);
        // 删除已关联的补助信息，并添加新的补助信息
        subsidyDao.deleteSubsidyInfos(travelIdList);
        for (SubsidyInfo subsidyInfo : subsidyInfos) {
            subsidyInfo.setTravelId(travelId);
            if (!subsidyDao.insertSubsidyInfo(subsidyInfo)) {
                isSuccess = false;
            }
        }
        // 将已关联该差旅信息的车票的关联字段设为空，将修改后的车票关联字段设为该差旅信息编号
        ticketDao.disassociateTickets(travelIdList);

        for (TicketResult ticket : tickets) {
            ticket.setTravelId(travelId);
            ticket.setTravelDesc(travelInfo.getTravelDesc());
            if (!ticketDao.updateTravelId(ticket)) {
                isSuccess = false;
            }
        }
        // 修改差旅信息中的出差说明、报销金额、出差时间等信息
        if (!travelInfoDao.updateTravelDesc(travelInfo)) {
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean associateTickets(AssociateTickets associateTickets) {
        // 如果差旅编号不存在，说明这是一条新增的关联
        if (associateTickets.getTravelForm().getTravelId() == null) {
            newAssociateTicket(associateTickets);
            List<Integer> travelIdList = new ArrayList<>();
            travelIdList.add(travelInfoDao.selectMaxTravelId());
            return travelInfoDao.updateStatus(travelIdList, 1);
        } else {
            // 否则说明这是在编辑已保存的差旅信息
            return editAssociateTicket(associateTickets);
        }
    }

    @Override
    public ResponseResult<AssociateTickets> getAssociateTickets(Integer travelId) {
        ResponseResult<AssociateTickets> responseResult =
                new ResponseResult<>();
        AssociateTickets associateTickets = new AssociateTickets();
        List<SubsidyInfo> subsidyInfos = null;
        List<TicketResult> tickets = null;
        TravelInfo travelInfo = null;
        // 车票编号
        List<Integer> tids = null;
        // 补助信息编号
        List<Integer> sids = null;
        // 查询差旅信息
        travelInfo = travelInfoDao.selectTravelInfo(travelId);
        travelInfo.setTravelId(travelId);
        // 查询补助信息
        subsidyInfos = subsidyDao.selectSubsidyInfosByTravelId(travelId);
        // 查询车票信息
        tickets = ticketDao.selectTicketsByTravelId(travelId);
        associateTickets.setTickets(tickets);
        associateTickets.setTravelForm(travelInfo);
        associateTickets.setSubsidyForm(subsidyInfos);
        responseResult.setData(associateTickets);
        responseResult.setCode(0);
        return responseResult;
    }

    @Override
    @Transactional
    public boolean delTravelInfo(List<Integer> travelIds) {
        boolean isSuccess = true;
        // 删除差旅信息
        if (!travelInfoDao.deleteTravelInfos(travelIds)) {
            isSuccess = false;
        }
        // 删除补助信息
        subsidyDao.deleteSubsidyInfos(travelIds);
        // 删除车票的关联字段
        if (!ticketDao.disassociateTickets(travelIds)) {
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public ResponseResult<List<ReimbursementResult>> getReimbursementList(SearchByStatus queryMap) {
        ResponseResult<List<ReimbursementResult>> responseResult = new ResponseResult<>();
        Integer page = queryMap.getPage();
        Integer limit = queryMap.getLimit();
        Integer status = queryMap.getStatus();
        List<TravelInfoResult> travelForms;
        List<ReimbursementResult> reimbursementResults = new ArrayList<>();
        if (page != null && limit != null) {
            IPage<TravelInfoResult> pages = new Page<>(page, limit);
            // 差旅信息
            travelInfoDao.selectTravelInfosByStatus(pages,
                    status);
            travelForms = pages.getRecords();
            if (travelForms != null) {
                for (TravelInfoResult travelForm : travelForms) {
                    int travelId = travelForm.getTravelId();
                    // 补助信息
                    List<SubsidyInfo> subsidyForm =
                            subsidyDao.selectSubsidyInfosByTravelId(travelId);
                    // 待关联的车票
                    List<TicketResult> tickets =
                            ticketDao.selectTicketsByTravelId(travelId);
                    ReimbursementResult reimbursementResult =
                            new ReimbursementResult(subsidyForm, travelForm, tickets);
                    reimbursementResults.add(reimbursementResult);
                }
                responseResult.setData(reimbursementResults);
                responseResult.setCode(0);
                responseResult.setCount((int) pages.getTotal());
            }
        } else {
            responseResult.setCode(500);
            responseResult.setMsg("请求参数有误！");
        }
        return responseResult;
    }

    @Override
    public boolean passReimbursement(PassForm passForm) {
        List<Integer> travelIds = passForm.getTravelIds();
        // 用户id以2开头说明是部门经理
        if (passForm.getUid().toString().startsWith("2")) {
            return travelInfoDao.updateStatus(travelIds, 5);
        } else {
            // 否则说明是财务
            return travelInfoDao.updateStatus(travelIds, 3);
        }
    }

    @Override
    public boolean rejectReimbursement(RejectForm rejectForm) {
        Integer travelId = rejectForm.getRejectTravelId();
        String rejectionReason = rejectForm.getRejectionReason();
        // 用户id以2开头说明是部门经理
        if (rejectForm.getUid().toString().startsWith("2")) {
            return travelInfoDao.updateStatusAndReason(travelId, 3.1, rejectionReason);
        } else {
            // 否则说明是财务
            return travelInfoDao.updateStatusAndReason(travelId, 2.1, rejectionReason);
        }
    }

    @Override
    public boolean submitReimbursement(AssociateTickets associateTickets) {
        TravelInfo travelInfo = associateTickets.getTravelForm();
        Integer travelId = travelInfo.getTravelId();
        // 差旅编号不为空，说明已经关联过车票信息，修改报销日期和状态信息即可
        if (travelId != null) {
            List<TicketResult> tickets = associateTickets.getTickets();
            List<SubsidyInfo> subsidyInfos = associateTickets.getSubsidyForm();
            // 补助信息关联的差旅编号
            Integer travelIdInSubsidyInfos = subsidyInfos.get(0).getTravelId();
            for (TicketResult ticket : tickets) {
                ticket.setTravelDesc(associateTickets.getTravelForm().getTravelDesc());
                ticketDao.updateTravelDesc(ticket);
            }
            // 如果差旅未关联补助信息则新增补助信息
            if (travelIdInSubsidyInfos == null) {
                for (SubsidyInfo subsidyInfo : subsidyInfos) {
                    subsidyInfo.setTravelId(travelId);
                    subsidyDao.insertSubsidyInfo(subsidyInfo);
                }
            }
            return travelInfoDao.updateSubmitDate(travelInfo);
        } else {
            // 否则直接新建关联，并添加报销日期
            newAssociateTicket(associateTickets);
            List<Integer> travelIdList = new ArrayList<>();
            travelIdList.add(travelInfoDao.selectMaxTravelId());
            return travelInfoDao.updateStatus(travelIdList, 2);
        }
    }
}
