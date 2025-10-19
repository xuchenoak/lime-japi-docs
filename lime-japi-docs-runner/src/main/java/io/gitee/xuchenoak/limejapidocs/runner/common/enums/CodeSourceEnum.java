package io.gitee.xuchenoak.limejapidocs.runner.common.enums;

import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.validatron.EnumValidator;

/**
 * 源码来源枚举
 **/
public enum CodeSourceEnum implements EnumValidator.EnumConverter<String> {

    LOCAL("本地"),
    GIT("git仓库"),

    ;
    private String value;

    CodeSourceEnum(String value) {
        this.value = value;
    }
    @Override
    public String value() {
        return value;
    }

    @Override
    public String msg() {
        return value;
    }

    public static CodeSourceEnum getEnum(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (CodeSourceEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }

}
