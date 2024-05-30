package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntValuesValidator implements ConstraintValidator<IntValuesValid, Integer> {

    /**
     * 验证值数组
     */
    private int[] values;

    @Override
    public void initialize(IntValuesValid constraintAnnotation) {
        values = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(Integer v, ConstraintValidatorContext context) {
        if (values == null || values.length == 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("未正确定义参数验证数组").addConstraintViolation();
            return false;
        } else {
            if (v != null) {
                for (int value : values) {
                    if (v.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
