package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * 消息工具类
 *
 * @author xuchenoak
 **/
public class MsgUtil {
    private static boolean parseStatus = false;
    private static DateTime parseTime;
    public static void statusParseRun() {
        parseStatus = true;
        parseTime = DateUtil.parse(DateUtil.date().toString("yyyy-MM-dd HH:mm:ss").concat(".000"));
    }

    public static void statusParseOver() {
        parseStatus = false;
    }

    public static boolean isParseRun() {
        return parseStatus;
    }

    public static DateTime getParseTime() {
        return parseTime;
    }

    public static Long getParseTimestamp() {
        if (parseTime == null) {
            return null;
        }
        return parseTime.getTime();
    }


}
