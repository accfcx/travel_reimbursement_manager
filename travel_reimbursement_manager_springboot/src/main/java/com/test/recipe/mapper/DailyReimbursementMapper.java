package com.test.recipe.mapper;

import com.test.recipe.model.DailyReimbursement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DailyReimbursementMapper {
    DailyReimbursement selectDailyReimbursement(Long id);
    DailyReimbursement findByRecipeId(Long id);
    List<DailyReimbursement> selectAllDailyReimbursements();
    int insertDailyReimbursement(DailyReimbursement dailyReimbursement);
    int updateDailyReimbursement(DailyReimbursement dailyReimbursement);
    int deleteDailyReimbursement(Long id);
}
