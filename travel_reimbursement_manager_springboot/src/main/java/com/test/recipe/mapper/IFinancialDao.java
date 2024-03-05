package com.test.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName IFinancialDao
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/1
 * @Version 1.0
 */
@Mapper
public interface IFinancialDao {
    String selectFinancialName(@Param("fid") Integer fid);
}
