package io.gitee.xuchenoak.limejapidocs.runner.util;

import java.util.function.Function;

/**
 * bean工具类
 **/
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {

    public static <T, R> R getBeanOrDoing(T bean, Function<T, R> f) {
        if (bean != null) {
            return f.apply(bean);
        }
        return null;
    }

}
