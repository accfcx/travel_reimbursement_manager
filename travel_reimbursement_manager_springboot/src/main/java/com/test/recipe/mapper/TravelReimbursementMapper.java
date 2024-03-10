package com.test.recipe.mapper;

import com.test.recipe.model.TravelReimbursement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelReimbursementMapper {
    TravelReimbursement selectTravelReimbursement(Long id);
    List<TravelReimbursement> selectAllTravelReimbursements();
    int insertTravelReimbursement(TravelReimbursement travelReimbursement);
    int updateTravelReimbursement(TravelReimbursement travelReimbursement);
    int deleteTravelReimbursement(Long id);
}
