package com.test.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IManagerDao {
    /**
     * @Author LiJian
     * @Description 查询管理员姓名
     * @Param 管理员编号
     * @Return 返回管理员姓名
     * @Date 2023/2/1 15:11
     * @Since version-1.0
     */
    String selectManagerName(@Param("mno") Integer mno);
}
