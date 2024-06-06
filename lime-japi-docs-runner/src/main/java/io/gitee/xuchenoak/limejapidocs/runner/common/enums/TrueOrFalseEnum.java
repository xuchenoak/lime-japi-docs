package io.gitee.xuchenoak.limejapidocs.runner.common.enums;

import io.gitee.xuchenoak.limejapidocs.runner.common.validatron.EnumValidator;

/**
 * 是或否枚举
 **/
public enum TrueOrFalseEnum implements EnumValidator.EnumConverter<Integer> {

    TRUE(1, "是"),
    FALSE(0, "否"),

    ;

    private int value;
    private String msg;
    TrueOrFalseEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static int getResolve(int status) {
        return status == TRUE.value ? FALSE.value : TRUE.value;
    }

    public static String getMsgByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (TrueOrFalseEnum e : TrueOrFalseEnum.values()) {
            if (value.equals(e.value)) {
                return e.msg;
            }
        }
        return null;
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public String msg() {
        return msg;
    }

    public boolean getBool() {
        return value == 1;
    }
}
