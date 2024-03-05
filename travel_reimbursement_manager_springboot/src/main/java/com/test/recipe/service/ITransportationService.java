package com.test.recipe.service;

import com.test.recipe.common.ResponseResult;
import com.test.recipe.model.Transport;
import com.test.recipe.vo.SearchByTransportation;

import java.util.List;

public interface ITransportationService {
    ResponseResult<List<Transport>> getAllTransportation();

    ResponseResult<List<Transport>> getTransportationListByPage(SearchByTransportation queryMap);

    boolean updateTransportation(Transport transportation);

    boolean addTransportation(Transport transportation);

    boolean deleteTransportation(List<Integer> transIds);
}
