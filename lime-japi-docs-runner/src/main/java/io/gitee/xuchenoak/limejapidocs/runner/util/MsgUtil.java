package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息工具类
 *
 * @author xuchenoak
 **/
public class MsgUtil {
    private static final Map<Long, Boolean> parseStatusMap = new ConcurrentHashMap<>();
    private static final Map<Long, DateTime> parseTimeMap = new ConcurrentHashMap<>();

    /**
     * 标记解析开始（原子抢占，避免并发重复触发）
     *
     * @param docsConfigId 文档配置Id
     * @return true-抢占成功；false-该文档正在解析中
     */
    public static synchronized boolean startParse(Long docsConfigId) {
        if (parseStatusMap.getOrDefault(docsConfigId, false)) {
            return false;
        }
        DateTime parseTime = DateUtil.parse(DateUtil.date().toString("yyyy-MM-dd HH:mm:ss").concat(".000"));
        parseStatusMap.put(docsConfigId, true);
        parseTimeMap.put(docsConfigId, parseTime);
        return true;
    }

    public static void statusParseRun(Long docsConfigId) {
        DateTime parseTime = DateUtil.parse(DateUtil.date().toString("yyyy-MM-dd HH:mm:ss").concat(".000"));
        parseStatusMap.put(docsConfigId, true);
        parseTimeMap.put(docsConfigId, parseTime);
    }

    public static void statusParseOver(Long docsConfigId) {
        parseStatusMap.put(docsConfigId, false);
    }

    /**
     * 清理解析状态（文档配置删除时调用，防止内存泄漏）
     *
     * @param docsConfigId 文档配置Id
     */
    public static void clear(Long docsConfigId) {
        parseStatusMap.remove(docsConfigId);
        parseTimeMap.remove(docsConfigId);
    }

    public static boolean isParseRun(Long docsConfigId) {
        return parseStatusMap.getOrDefault(docsConfigId, false);
    }

    public static DateTime getParseTime(Long docsConfigId) {
        return parseTimeMap.get(docsConfigId);
    }

    public static Long getParseTimestamp(Long docsConfigId) {
        DateTime parseTime = parseTimeMap.get(docsConfigId);
        if (parseTime == null) {
            return null;
        }
        return parseTime.getTime();
    }


}