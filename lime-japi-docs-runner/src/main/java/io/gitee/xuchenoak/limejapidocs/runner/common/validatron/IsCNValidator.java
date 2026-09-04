package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsCNValidator implements ConstraintValidator<IsCN, String> {

    String regex = "^[a-z0-9A-Z\u4e00-\u9fa5\uff0c]+$";
    private String message;
    private Pattern moneyPattern = Pattern.compile(regex);

    @Override
    public void initialize(IsCN constraintAnnotation) {
        //获取注解上的值，可以做一写取注解上的值做一下判断
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            //是空的，返回true，是因为如果null，则会有@NotBlank进行提示，如果没有 @NotBlank进行提示则返回false
            return true;
        }
        boolean result = moneyPattern.matcher(value).matches();
        if (!result) {
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return result;
    }
}
