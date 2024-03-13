package com.test.recipe.mapper;

import com.test.recipe.model.Invoice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author accfcx
 **/
@Mapper
public interface InvoiceMapper {
    void insert(Invoice invoice);
    void update(Invoice invoice);
    void deleteById(long id);
    Invoice findById(long id);
    Invoice findByNum(String num);
    List<Invoice> findAll();
    List<Invoice> findByReceiptId(long receiptId);
}