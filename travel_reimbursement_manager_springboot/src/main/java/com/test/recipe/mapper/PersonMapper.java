package com.test.recipe.mapper;


import com.test.recipe.model.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author accfcx
 **/
@Mapper
public interface PersonMapper {
    void insert(Person person);

    void update(Person person);

    void deleteById(long id);

    Person findById(long id);

    Person findByName(String  username);

    List<Person> findAll();

    List<Person> findByDepartmentId(long departmentId);
}
