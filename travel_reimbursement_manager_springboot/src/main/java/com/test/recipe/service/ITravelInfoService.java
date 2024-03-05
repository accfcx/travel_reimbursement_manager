package com.test.recipe.service;

import com.test.recipe.common.ResponseResult;
import com.test.recipe.dto.ReimbursementResult;
import com.test.recipe.model.TravelInfo;
import com.jxd.travel.vo.*;
import com.test.recipe.vo.*;

import java.util.List;

public interface ITravelInfoService {
    /**
     * @Author LiJian
     * @Description 获取差旅报销信息
     * @Param SearchByTime - 查询条件
     * @Return 返回响应信息
     * @Date 2023/2/1 19:18
     * @Since version-1.0
     */
    ResponseResult<List<TravelInfo>> getTravelInfoList(SearchByTime queryMap);

    /**
     * @Author LiJian
     * @Description 获取差旅报销状态信息
     * @Param Integer - 差旅信息所属的员工编号
     * @Return  返回差旅报销状态信息的集合
     * @Date 2023/2/1 23:58
     * @Since version-1.0
     */
    ResponseResult<List<Double>> getProcess(Integer empno);

    /**
     * @Author LiJian
     * @Description 保存差旅信息（新增或修改）
     * @Param AssociateTickets - 关联信息
     * @Return 返回是否关联成功
     * @Date 2023/2/2 15:46
     * @Since version-1.0
     */
    boolean associateTickets(AssociateTickets associateTickets);

    /**
     * @Author LiJian
     * @Description 获取差旅信息即其对应的车票、补助等
     * @Param Integer - 差旅编号
     * @Return 返回响应信息
     * @Date 2023/2/3 9:47
     * @Since version-1.0
     */
    ResponseResult<AssociateTickets> getAssociateTickets(Integer travelId);

    /**
     * @Author LiJian
     * @Description 删除指定差旅信息
     * @Param List - 差旅信息编号
     * @Return 返回是否删除成功
     * @Date 2023/2/3 11:34
     * @Since version-1.0
     */
    boolean delTravelInfo(List<Integer> travelIds);

    /**
     * @Author LiJian
     * @Description 查询报销信息
     * @Param SearchByStatus - 查询条件
     * @Return 返回响应信息
     * @Date 2023/2/4 6:17
     * @Since version-1.0
     */
    ResponseResult<List<ReimbursementResult>> getReimbursementList(SearchByStatus queryMap);

    /**
     * @Author LiJian
     * @Description 通过报销
     * @Param PassForm - 通过信息
     * @Return 返回是否通过成功
     * @Date 2023/2/4 10:26
     * @Since version-1.0
     */
    boolean passReimbursement(PassForm passForm);

    /**
     * @Author LiJian
     * @Description 驳回报销
     * @Param RejectForm - 驳回信息
     * @Return 返回是否驳回成功
     * @Date 2023/2/4 12:04
     * @Since version-1.0
     */
    boolean rejectReimbursement(RejectForm rejectForm);

    /**
     * @Author LiJian
     * @Description 提交差旅报销
     * @Param TravelInfo - 差旅关联信息
     * @Return 返回是否提交成功
     * @Date 2023/2/4 13:34
     * @Since version-1.0
     */
    boolean submitReimbursement(AssociateTickets associateTickets);
}
