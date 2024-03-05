package com.test.recipe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.recipe.common.ResponseResult;
import com.test.recipe.model.Emp;
import com.test.recipe.model.User;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IEmpService
 * @Description TODO
 * @Author LiJian
 * @Date 2023/1/28
 * @Version 1.0
 */
public interface IEmpService extends IService<Emp> {
    /**
     * @Author LiJian
     * @Description 登录
     * @Param User - 用户
     * @Return 返回响应信息
     * @Date 2023/1/28 14:31
     * @Since version-1.0
     */
    ResponseResult<String> login(User user);

    /**
     * @Author LiJian
     * @Description 分页查询所有员工
     * @Param Map - 查询条件
     * @Return 返回分页查询结果
     * @Date 2023/1/29 16:42
     * @Since version-1.0
     */
    ResponseResult<List<Emp>> getEmps(Map<String, String> queryMap);

    /**
     * @Author LiJian
     * @Description 新增员工
     * @Param Emp - 要新增的员工
     * @Return 返回是否新增成功
     * @Date 2023/1/29 11:25
     * @Since version-1.0
     */
    boolean addEmp(Emp emp);

    /**
     * @Author LiJian
     * @Description 修改员工信息
     * @Param Emp - 要修改的员工
     * @Return 返回是否修改成功
     * @Date 2023/1/30 17:31
     * @Since version-1.0
     */
    boolean updateEmp(Emp emp);

    /**
     * @Author LiJian
     * @Description 删除员工信息
     * @Param Integer[] - 要删除员工的员工编号
     * @Return 返回是否删除成功
     * @Date 2023/1/30 17:54
     * @Since version-1.0
     */
    boolean deleteBatch(List<Integer> empnos);
}
