package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=IsCNValidator.class)
public @interface IsCN {


    String message() default"内容输入错误,只允许输入中文或字母和,";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
