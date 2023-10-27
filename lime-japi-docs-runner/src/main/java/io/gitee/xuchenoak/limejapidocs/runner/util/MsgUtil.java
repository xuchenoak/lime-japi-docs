package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息工具类
 *
 * @author xuchenoak
 **/
public class MsgUtil {
    private static final Map<Long, Boolean> parseStatusMap = new HashMap<>();
    private static final Map<Long, DateTime> parseTimeMap = new HashMap<>();
    public static void statusParseRun(Long docsConfigId) {
        DateTime parseTime = DateUtil.parse(DateUtil.date().toString("yyyy-MM-dd HH:mm:ss").concat(".000"));
        parseStatusMap.put(docsConfigId, true);
        parseTimeMap.put(docsConfigId, parseTime);
    }

    public static void statusParseOver(Long docsConfigId) {
        parseStatusMap.put(docsConfigId, false);
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
