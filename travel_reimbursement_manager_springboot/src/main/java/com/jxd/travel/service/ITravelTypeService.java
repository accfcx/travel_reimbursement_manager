package com.jxd.travel.service;

import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.model.TravelType;

import java.util.List;

public interface ITravelTypeService {
    /**
     * @Author LiJian
     * @Description 获取出差方式
     * @Return 返回响应结果
     * @Date 2023/1/31 14:43
     * @Since version-1.0
     */
    ResponseResult<List<TravelType>> getTravelTypeList();
}
