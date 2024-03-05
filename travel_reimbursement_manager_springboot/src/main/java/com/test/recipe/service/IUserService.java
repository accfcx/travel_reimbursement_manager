package com.test.recipe.service;

import com.test.recipe.vo.ChangePwd;

public interface IUserService {
    /**
     * @Author LiJian
     * @Description 重置密码
     * @Param Integer - 员工编号，即用户id
     * @Return 返回是否重置成功
     * @Date 2023/2/1 0:09
     * @Since version-1.0
     */
    boolean resetPwd(Integer uid);

    /**
     * @Author LiJian
     * @Description 修改密码
     * @Param ChangePwd - 修改密码用到的信息
     * @Return 返回修改信息
     * @Date 2023/2/5 16:36
     * @Since version-1.0
     */
    String changePwd(ChangePwd pwdForm);
}
