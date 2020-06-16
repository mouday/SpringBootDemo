package org.example.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    // 返回校验结果
    public ValidationResult validate(Object bean) {
        ValidationResult result = new ValidationResult();

        Set<ConstraintViolation<Object>> constraintViolationSet = this.validator.validate(bean);

        System.out.println("validate");

        // 有错误
        if (constraintViolationSet.size() > 0) {
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation -> {
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();

                result.getErrorMsgMap().put(propertyName, errMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 通过工厂初始化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
