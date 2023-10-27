package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntLenValidator implements ConstraintValidator<IntLenValid, Integer> {

    /** 最小值 */
    private int min;

    /** 最大值 */
    private int max;

    @Override
    public void initialize(IntLenValid constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer v, ConstraintValidatorContext context) {
        if (v == null) {
            return false;
        }
        return min <= v && max >= v;
    }
}
