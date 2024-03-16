package com.test.recipe.controller;

import com.test.recipe.mapper.BankCardMapper;
import com.test.recipe.mapper.PersonMapper;
import com.test.recipe.model.BankCard;
import com.test.recipe.model.Person;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author accfcx
 **/
@RestController
@RequestMapping("/setting")
public class SettingController {
    @Resource
    BankCardMapper bankCardMapper;


    // 银行卡列表
    @GetMapping("/bankCard/List/{uid}")
    public Object bankCardList(@PathVariable Long uid) {
        return bankCardMapper.findByOwnerId(uid);
    }

    // 新增
    @PostMapping("/bankCard/add")
    public Object bankCardAdd(@RequestBody BankCard bankCard) {
        bankCard.setCreateTime(new Date());
        bankCard.setUpdateTime(new Date());
        bankCardMapper.insert(bankCard);
        return "suc";
    }

    // 更新
    @PostMapping("/bankCard/update")
    public Object bankCardUpdate(@RequestBody BankCard bankCard) {
        bankCardMapper.update(bankCard);
        return "suc";
    }

    // 删除
    @PostMapping("/bankCard/del")
    public Object bankCardDel(@RequestBody BankCard bankCard) {
        bankCardMapper.deleteById(bankCard.getId());
        return "suc";
    }
}
