package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import java.util.List;

/**
 * 解析日志业务接口
 *
 * @author xuchenoak
 **/
public interface ApiDocsParseLogService {

    /**
     * 添加消息
     * @param msg 消息内容
     */
    void addMsg(String msg);

    /**
     * 获取消息内容
     * @param parseTimestamp 解析时间戳
     * @return
     */
    List<String> listMsg(Long parseTimestamp);

}
