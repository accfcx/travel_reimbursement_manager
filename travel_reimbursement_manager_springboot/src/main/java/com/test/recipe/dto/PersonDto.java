package com.test.recipe.dto;

import com.test.recipe.model.Department;
import com.test.recipe.model.Permission;
import com.test.recipe.model.Person;
import lombok.Data;

@Data
public class PersonDto {

    Person person;

    Department department;

    Permission permission;

}
