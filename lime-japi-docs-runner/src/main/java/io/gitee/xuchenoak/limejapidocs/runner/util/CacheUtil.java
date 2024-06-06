package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.json.JSONUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 描述
 *
 * @author xuchenoak
 **/
public class CacheUtil {

    private static final TimedCache<String, String> timedCache = cn.hutool.cache.CacheUtil.newTimedCache(1000 * 60 * 30);

    static {
        timedCache.schedulePrune(1000 * 60);
    }

    public static void remove(String k) {
        timedCache.remove(k);
    }

    public static void set(String k, String v) {
        timedCache.put(k, v);
    }

    public static void set(String k, String v, long timeout, TimeUnit timeUnit) {
        timedCache.put(k, v, timeUnit.toMillis(timeout));
    }

    public static String get(String k) {
        return timedCache.get(k);
    }

    public static <T> T getBean(String k, Class<T> c) {
        try {
            return JSONUtil.toBean(timedCache.get(k), c);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> getBeanList(String k, Class<T> c) {
        try {
            return JSONUtil.toList(timedCache.get(k), c);
        } catch (Exception e) {
            return null;
        }
    }

}
