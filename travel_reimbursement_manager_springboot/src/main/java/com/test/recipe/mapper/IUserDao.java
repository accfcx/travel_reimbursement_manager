package com.test.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IUserDao {
    /**
     * @Author LiJian
     * @Description 重置密码
     * @Param Integer - 员工编号，即用户id
     * @Return 返回是否重置成功
     * @Date 2023/2/1 0:10
     * @Since version-1.0
     */
    boolean resetPwd(@Param("uid") Integer uid);

    boolean insertUser(@Param("uid") Integer uid);

    boolean deleteUsers(@Param("uids") List<Integer> uids);

    String selectPasswordById(@Param("uid") Integer id);

    boolean changePwd(@Param("uid") Integer uid,
                      @Param("newPwd") String newPwd);
}
