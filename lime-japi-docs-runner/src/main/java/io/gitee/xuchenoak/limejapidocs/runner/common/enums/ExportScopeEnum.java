package io.gitee.xuchenoak.limejapidocs.runner.common.enums;

import io.gitee.xuchenoak.limejapidocs.runner.common.validatron.EnumValidator;

/**
 * 文档导出范围枚举
 *
 * @author xuchenoak
 **/
public enum ExportScopeEnum implements EnumValidator.EnumConverter<String> {

    /**
     * 整个文档
     */
    PROJECT("project", "整个文档"),

    /**
     * 单个controller
     */
    CONTROLLER("controller", "单个controller"),

    /**
     * 单个接口
     */
    INTERFACE("interface", "单个接口"),

    ;

    /**
     * 导出范围值
     */
    private final String value;

    /**
     * 导出范围描述
     */
    private final String msg;

    ExportScopeEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 导出范围值
     * @return 枚举对象
     */
    public static ExportScopeEnum getEnum(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        for (ExportScopeEnum e : values()) {
            if (e.value.equalsIgnoreCase(value.trim())) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String msg() {
        return msg;
    }
}
