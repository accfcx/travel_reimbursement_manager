package com.test.recipe.mapper;

import com.test.recipe.model.OvertimeApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OvertimeApplyMapper {
    OvertimeApply selectOvertimeApply(Long id);
    OvertimeApply findByRecipeId(Long id);
    List<OvertimeApply> selectAllOvertimeApplys();
    int insertOvertimeApply(OvertimeApply overtimeApply);
    int updateOvertimeApply(OvertimeApply overtimeApply);
    int deleteOvertimeApply(Long id);
}
