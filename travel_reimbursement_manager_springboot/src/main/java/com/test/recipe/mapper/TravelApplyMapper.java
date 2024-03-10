package com.test.recipe.mapper;

import com.test.recipe.model.TravelApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelApplyMapper {
    TravelApply selectTravelApply(Long id);
    List<TravelApply> selectAllTravelApplys();
    int insertTravelApply(TravelApply travelApply);
    int updateTravelApply(TravelApply travelApply);
    int deleteTravelApply(Long id);
}
