package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证最大最小值
 */
@Documented
@Constraint(validatedBy = {IntLenValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE, PARAMETER})
@Retention(RUNTIME)
public @interface IntLenValid {

    String message() default "参数不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 最小值 */
    int min() default 0;

    /** 最大值 */
    int max() default 0;

}
