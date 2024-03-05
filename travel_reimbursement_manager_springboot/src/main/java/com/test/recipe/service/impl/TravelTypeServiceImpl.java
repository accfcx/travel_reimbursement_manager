package com.test.recipe.service.impl;

import com.test.recipe.common.ResponseResult;
import com.test.recipe.mapper.ITravelTypeDao;
import com.test.recipe.model.TravelType;
import com.test.recipe.service.ITravelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TravelTypeServiceImpl
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/31
 * @Version 1.0
 */
@Service
public class TravelTypeServiceImpl implements ITravelTypeService {
    @Autowired
    private ITravelTypeDao travelTypeDao;

    @Override
    public ResponseResult<List<TravelType>> getTravelTypeList() {
        ResponseResult<List<TravelType>> responseResult =
                new ResponseResult<>();
        List<TravelType> travelTypeList = travelTypeDao.selectTravelTypes();
        responseResult.setData(travelTypeList);
        responseResult.setCode(0);
        return responseResult;
    }
}
