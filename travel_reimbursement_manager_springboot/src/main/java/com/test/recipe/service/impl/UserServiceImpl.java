package com.test.recipe.service.impl;

import com.test.recipe.mapper.IUserDao;
import com.test.recipe.service.IUserService;
import com.test.recipe.vo.ChangePwd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author LiJian
 * @Date 2023/2/1
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Override
    public boolean resetPwd(Integer uid) {
        return userDao.resetPwd(uid);
    }

    @Override
    public String changePwd(ChangePwd pwdForm) {
        Integer uid = pwdForm.getUid();
        String oldPwd = pwdForm.getOldPwd();
        String newPwd = pwdForm.getNewPwd();
        if(!oldPwd.equals(userDao.selectPasswordById(uid))){
            return "旧密码输入错误~";
        }
        if (userDao.changePwd(uid, newPwd)) {
            return "成功";
        } else {
            return "失败";
        }
    }
}
