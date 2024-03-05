package com.test.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName IDeptHeadDao
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/1
 * @Version 1.0
 */
@Mapper
public interface IDeptHeadDao {
    String selectDeptHeadName(@Param("hid") Integer hid);
}
