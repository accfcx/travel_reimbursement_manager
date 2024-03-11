package com.test.recipe.mapper;

import com.test.recipe.model.ProcessDef;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author accfcx
 **/
@Mapper
public interface ProcessDefMapper {

    boolean insert(ProcessDef processDef);

    ProcessDef findById(Long id);

    ProcessDef findByType(String recipeType);

    void update(ProcessDef processDef);

    List<ProcessDef> findAll();

}