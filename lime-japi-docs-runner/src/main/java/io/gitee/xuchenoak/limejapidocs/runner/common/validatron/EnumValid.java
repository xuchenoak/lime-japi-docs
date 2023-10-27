package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证枚举对象属性，枚举必须实现ConstantEnumValidator.EnumConverter接口
 */
@Documented
@Constraint(validatedBy = {EnumValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE, PARAMETER})
@Retention(RUNTIME)
public @interface EnumValid {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends EnumValidator.EnumConverter> enumClass();

}
