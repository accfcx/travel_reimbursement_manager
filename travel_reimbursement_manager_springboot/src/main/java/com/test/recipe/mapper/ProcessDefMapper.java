package com.test.recipe.mapper;

import com.test.recipe.model.ProcessDef;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author accfcx
 **/
@Mapper
public interface ProcessDefMapper {

    boolean insert(ProcessDef pmsModel);

    ProcessDef findById(Long id);

    void update(ProcessDef pmsModel);

    List<ProcessDef> findAll();

}