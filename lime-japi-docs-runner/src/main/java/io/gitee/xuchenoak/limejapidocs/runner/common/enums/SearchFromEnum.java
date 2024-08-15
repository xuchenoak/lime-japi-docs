package io.gitee.xuchenoak.limejapidocs.runner.common.enums;

import io.gitee.xuchenoak.limejapidocs.runner.common.validatron.EnumValidator;

/**
 * 搜索来源枚举
 **/
public enum SearchFromEnum implements EnumValidator.EnumConverter<Integer> {

    ALL(1, "所有"),
    CATALOG(2, "目录"),
    INTERFACE(3, "接口"),

    ;

    private int value;
    private String msg;

    SearchFromEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static SearchFromEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (SearchFromEnum e : SearchFromEnum.values()) {
            if (value.equals(e.value)) {
                return e;
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
}
