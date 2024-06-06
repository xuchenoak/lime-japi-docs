package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, Object> {

    /**
     * 枚举对象数组
     */
    private EnumConverter[] constants;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        try {
            Class<? extends EnumConverter> constant = constraintAnnotation.enumClass();
            constants = (EnumConverter[]) constant.getMethod("values").invoke(null);
        } catch (Exception e) {
            constants = null;
        }
    }

    @Override
    public boolean isValid(Object v, ConstraintValidatorContext context) {
        StringBuilder msgBuilder = new StringBuilder();
        if (constants == null) {
            msgBuilder.append("未正确定义参数验证规则");
        } else {
            if (v != null) {
                for (EnumConverter constant : constants) {
                    if (constant.value() != null && constant.value().equals(v)) {
                        return true;
                    }
                }
            }
            msgBuilder.append("参数必须位于：");
            for (EnumConverter constant : constants) {
                msgBuilder.append(constant.value());
                if (constant.msg() != null && constant.msg().length() > 0) {
                    msgBuilder.append("-").append(constant.msg()).append("，");
                } else {
                    msgBuilder.append("，");
                }
            }
        }
        String msg = context.getDefaultConstraintMessageTemplate();
        if (msg == null || msg.trim().length() == 0) {
            context.disableDefaultConstraintViolation();
            msg = msgBuilder.toString();
            if (msg.length() > 0 && msg.endsWith("，")) {
                msg = msg.substring(0, msg.length() - 1);
            }
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        }
        return false;
    }

    /**
     * 枚举验证接口
     **/
    public interface EnumConverter<T> {

        /**
         * 值
         *
         * @return
         */
        T value();

        /**
         * 值描述
         *
         * @return
         */
        String msg();

    }
}
