package com.jxd.travel.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jxd.travel.model.Transport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ITransportationDao
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/31
 * @Version 1.0
 */
@Mapper
public interface ITransportationDao {
    List<Transport> selectAllTransportation();

    IPage<Transport> selectTranportationListByPage(IPage<Transport> pages,
                                                   @Param("transportation") String transportation);

    boolean updateTransportation(@Param("transport") Transport transport);

    boolean addTransportation(@Param("transport") Transport transport);

    boolean deleteTransportation(@Param("transIds") List<Integer> transIds);
}
