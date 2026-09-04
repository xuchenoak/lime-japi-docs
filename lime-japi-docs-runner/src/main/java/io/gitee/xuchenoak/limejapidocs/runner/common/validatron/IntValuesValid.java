package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证是否在给定数组中（values={0,1}, message="参数必须位于：0，1"）
 */
@Documented
@Constraint(validatedBy = {IntValuesValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE, PARAMETER})
@Retention(RUNTIME)
public @interface IntValuesValid {

    String message() default "参数不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};

}
