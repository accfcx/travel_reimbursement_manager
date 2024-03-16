package com.test.recipe.mapper;

import com.test.recipe.model.FeeItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author accfcx
 **/
@Mapper
public interface FeeItemMapper {
    void insert(FeeItem feeItem);

    void update(FeeItem feeItem);

    void deleteById(long id);

    FeeItem findById(long id);

    List<FeeItem> findAll();

    FeeItem findByReceiptId(long id);

    List<Map<String, Object>> aggByMonth();

    List<Map<String, Object>> aggByFeeType();
}