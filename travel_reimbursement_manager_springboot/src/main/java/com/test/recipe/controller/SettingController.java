package com.test.recipe.controller;

import com.test.recipe.mapper.BankCardMapper;
import com.test.recipe.mapper.PersonMapper;
import com.test.recipe.model.BankCard;
import com.test.recipe.model.Person;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author accfcx
 **/
@RestController
@RequestMapping("/setting")
public class SettingController {
    @Resource
    BankCardMapper bankCardMapper;

    @Resource
    PersonMapper personMapper;

    // 银行卡列表
    @PostMapping("/bankCard/List?username={username}")
    public Object bankCardList(@PathVariable String username) {

        Person person = personMapper.findByName(username);
        return bankCardMapper.findByOwnerId(person.getId());
    }

    // 新增
    @PostMapping("/bankCard/add")
    public Object bankCardAdd(@RequestBody BankCard bankCard) {
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
