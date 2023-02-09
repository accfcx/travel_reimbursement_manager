package com.jxd.travel.dao;

import com.jxd.travel.model.SubsidyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ISubsidyDao {
    boolean insertSubsidyInfo(@Param("subsidyInfo")SubsidyInfo subsidyInfo);

    List<SubsidyInfo> selectSubsidyInfosByTravelId(@Param("travelId") Integer travelId);

    boolean deleteSubsidyInfos(@Param("travelIds") List<Integer> travelIds);
}
