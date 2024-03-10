package com.test.recipe.mapper;


import com.test.recipe.model.BankCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author accfcx
 **/
@Mapper
public interface BankCardMapper {
    void insert(BankCard bankCard);
    void update(BankCard bankCard);
    void deleteById(long id);
    BankCard findById(long id);
    List<BankCard> findAll();
    List<BankCard> findByOwnerId(long ownerId);
}