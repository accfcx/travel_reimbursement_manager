package com.test.recipe.mapper;


import com.test.recipe.model.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author accfcx
 **/
@Mapper
public interface PermissionMapper {
    void insert(Permission permission);
    void update(Permission permission);
    void deleteById(long id);
    Permission findById(long id);
    Permission findByName(String name);
    List<Permission> findAll();
}