package com.jxd.travel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jxd.travel.model.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IEmpDao extends BaseMapper<Emp> {
    /**
     * @Author LiJian
     * @Description &#x65B0;&#x589E;&#x5458;&#x5DE5;
     * @Param Emp - &#x8981;&#x65B0;&#x589E;&#x7684;&#x5458;&#x5DE5;
     * @Return &#x8FD4;&#x56DE;&#x662F;&#x5426;&#x65B0;&#x589E;&#x6210;&#x529F;
     * @Date 2023/1/29 11:00
     * @Since version-1.0
     */
    boolean insertEmp(Emp emp);

    /**
     * @Author LiJian
     * @Description 分页查询
     * @Param IPage - 分页数据
     * @Param String - 员工姓名
     * @Return 返回分页数据
     * @Date 2023/1/29 23:02
     * @Since version-1.0
     */
    IPage<Emp> selectEmps(IPage<Emp> page, @Param("ename") String ename);

    /**
     * @Author LiJian
     * @Description 查询最大的员工编号
     * @Return 返回最大的员工编号
     * @Date 2023/1/29 23:03
     * @Since version-1.0
     */
    Integer selectMaxEmpno();

    /**
     * @Author LiJian
     * @Description 更新员工数据
     * @Param Emp - 要更新的员工信息
     * @Return 返回是否更新成功
     * @Date 2023/1/30 17:55
     * @Since version-1.0
     */
    boolean updateEmp(Emp emp);

    boolean deleteEmps(@Param("empnos") List<Integer> empnos);

    String selectEmpName(@Param("empno") Integer empno);
}
