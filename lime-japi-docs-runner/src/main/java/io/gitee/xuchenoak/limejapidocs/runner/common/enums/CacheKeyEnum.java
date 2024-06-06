package io.gitee.xuchenoak.limejapidocs.runner.common.enums;

import cn.hutool.core.util.StrUtil;

/**
 * 缓存标识
 *
 * @author xuchenoak
 **/
public enum CacheKeyEnum {

    // 系统参数
    Param,

    // 系统配置文件
    SysConfigProperties,

    ;

    public String getKey() {
        return this.toString();
    }

    public String getKey(String str) {
        return StrUtil.format("{}:{}", this.toString(), str);
    }

}
