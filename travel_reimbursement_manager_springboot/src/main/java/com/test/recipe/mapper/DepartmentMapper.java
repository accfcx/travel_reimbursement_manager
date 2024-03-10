package com.test.recipe.mapper;

import com.test.recipe.model.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author accfcx
 **/
@Mapper
public interface DepartmentMapper {
    void insert(Department department);

    void update(Department department);

    void deleteById(long id);

    Department findById(long id);

    List<Department> findAll();
}