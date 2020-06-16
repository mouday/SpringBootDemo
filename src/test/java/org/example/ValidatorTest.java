package org.example;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

class Person {
    @NotBlank
    @NotNull
    private String name;

    @Min(0)
    @Max(150)
    private int age;

    @NotNull
    private boolean gender;

    public Person(String name, int age, boolean gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}

public class ValidatorTest {
    public static void main(String[] args) {


        // 获取验证器
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // 数据验证
        Person person = new Person("", 151, false);
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        // 打印验证结果
        for(ConstraintViolation violation: violations){
            System.out.println(violation.getPropertyPath() + violation.getMessage());
        }
        /**
         * age最大不能超过150
         * name不能为空
         */

    }
}
