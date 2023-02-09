package com.jxd.travel.service;

import com.jxd.travel.common.ResponseResult;
import com.jxd.travel.model.Transport;
import com.jxd.travel.vo.SearchByTransportation;

import java.util.List;

public interface ITransportationService {
    ResponseResult<List<Transport>> getAllTransportation();

    ResponseResult<List<Transport>> getTransportationListByPage(SearchByTransportation queryMap);

    boolean updateTransportation(Transport transportation);

    boolean addTransportation(Transport transportation);

    boolean deleteTransportation(List<Integer> transIds);
}
