package com.test.recipe.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.test.recipe.dto.TravelInfoResult;
import com.test.recipe.model.TravelInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ITravelInfoDao {
    /**
     * @Author LiJian
     * @Description &#x67E5;&#x8BE2;&#x6307;&#x5B9A;&#x5458;&#x5DE5;&#x7684;&#x5DEE;&#x65C5;&#x62A5;&#x9500;&#x4FE1;&#x606F;
     * @Param Integer - &#x5458;&#x5DE5;&#x7F16;&#x53F7;
     * @Return &#x8FD4;&#x56DE;&#x67E5;&#x8BE2;&#x5230;&#x7684;&#x5DEE;&#x65C5;&#x62A5;&#x9500;&#x4FE1;&#x606F;&#x96C6;&#x5408;
     * @Date 2023/2/1 19:21
     * @Since version-1.0
     */
    IPage<TravelInfo> selectTravelInfos(IPage<TravelInfo> pages,
                                        @Param("reimbursementDate") String reimbursementDate,
                                        @Param("empno") Integer empno);

    /**
     * @Author LiJian
     * @Description 查询差旅报销状态
     * @Param Integer - 差旅报销所属的员工编号
     * @Return 返回差旅报销状态的集合
     * @Date 2023/2/2 0:03
     * @Since version-1.0
     */
    List<Double> selectStatus(@Param("empno") Integer empno);

    /**
     * @Author LiJian
     * @Description 查询最大的差旅信息id
     * @Return 返回最大的差旅信息id
     * @Date 2023/2/2 15:49
     * @Since version-1.0
     */
    Integer selectMaxTravelId();

    /**
     * @Author LiJian
     * @Description 新增一条差旅信息
     * @Param TravelInfo - 差旅信息
     * @Return 返回是否新增成功
     * @Date 2023/2/2 15:54
     * @Since version-1.0
     */
    boolean insertTravelInfo(@Param("travelInfo") TravelInfo travelInfo);

    /**
     * @Author LiJian
     * @Description 根据差旅编号查询差旅信息
     * @Param Integer - 差旅编号
     * @Return 返回差旅信息
     * @Date 2023/2/3 9:59
     * @Since version-1.0
     */
    TravelInfo selectTravelInfo(@Param("travelId") Integer travelId);

    /**
     * @Author LiJian
     * @Description 删除指定差旅信息
     * @Param 差旅信息编号集合
     * @Return 返回是否删除成功
     * @Date 2023/2/3 11:33
     * @Since version-1.0
     */
    boolean deleteTravelInfos(@Param("travelIds") List<Integer> travelIds);

    /**
     * @Author LiJian
     * @Description 修改出差说明
     * @Param Integer - 差旅编号
     * @Return 返回是否修改成功
     * @Date 2023/2/3 15:51
     * @Since version-1.0
     */
    boolean updateTravelDesc(@Param("travelInfo") TravelInfo travelInfo);

    /**
     * @Author LiJian
     * @Description 根据报销状态查询差旅信息
     * @Param IPage - 分页对象
     * @Param Integer - 报销状态
     * @Return 返回差旅信息集合
     * @Date 2023/2/4 6:26
     * @Since version-1.0
     */
    IPage<TravelInfoResult> selectTravelInfosByStatus(IPage<TravelInfoResult> pages, @Param("status") Integer status);

    /**
     * @Author LiJian
     * @Description 更新报销状态
     * @Param List - 要更新的差旅编号
     * @Return 返回是否更新成功
     * @Date 2023/2/4 10:28
     * @Since version-1.0
     */
    boolean updateStatus(@Param("travelIds") List<Integer> travelIds, @Param(
            "status") Integer status);

    /**
     * @Author LiJian
     * @Description 更新报销状态和驳回原因
     * @Param Double - 报销状态
     * @Param Integer - 差旅编号
     * @Param String - 驳回原因
     * @Return 返回是否更新成功
     * @Date 2023/2/4 12:10
     * @Since version-1.0
     */
    boolean updateStatusAndReason(@Param("travelId") Integer travelId,
                                  @Param("status") Double status,
                                  @Param("rejectionReason") String rejectionReason);

    /**
     * @Author LiJian
     * @Description 更新报销提交时间
     * @Param Integer - 差旅编号
     * @Param String - 提交时间
     * @Return 返回是否更新成功
     * @Date 2023/2/4 13:39
     * @Since version-1.0
     */
    boolean updateSubmitDate(@Param("travelInfo") TravelInfo travelInfo);
}
