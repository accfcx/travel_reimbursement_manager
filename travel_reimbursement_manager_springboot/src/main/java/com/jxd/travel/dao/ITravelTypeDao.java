package com.jxd.travel.dao;

import com.jxd.travel.model.TravelType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITravelTypeDao {
    /**
     * @Author LiJian
     * @Description 查询出差类型
     * @Return 返回出差类型的集合
     * @Date 2023/1/31 14:46
     * @Since version-1.0
     */
    List<TravelType> selectTravelTypes();
}
