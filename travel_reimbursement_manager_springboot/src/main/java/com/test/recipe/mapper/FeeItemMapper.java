package com.test.recipe.mapper;

import com.test.recipe.model.FeeItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    List<FeeItem> findByReceiptId(long receiptId);
}