package io.gitee.xuchenoak.limejapidocs.runner.service.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.EntityBaseService;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsParseLog;
import io.gitee.xuchenoak.limejapidocs.runner.mapper.ApiDocsParseLogMapper;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析日志业务实现
 *
 * @author xuchenoak
 **/
@Service
public class ApiDocsParseLogService extends EntityBaseService<ApiDocsParseLogMapper, ApiDocsParseLog> {

    /**
     * 添加消息
     * @param docsConfigId 文档配置Id
     * @param msg 消息内容
     */
    public void addMsg(Long docsConfigId, String msg) {
        msg = StrUtil.format("[{}]  {}", DateUtil.date().toString("yyyy-MM-dd HH:mm:ss"), msg);
        save(new ApiDocsParseLog(0L, docsConfigId, MsgUtil.getParseTimestamp(docsConfigId), msg, System.currentTimeMillis()));
    }

    /**
     * 获取消息内容
     * @param docsConfigId 文档配置Id
     * @param parseTimestamp 解析时间戳
     * @return
     */
    public List<String> listMsg(Long docsConfigId, Long parseTimestamp) {
        List<String> msgList = new ArrayList<>();
        List<ApiDocsParseLog> list = list(new LambdaQueryWrapper<ApiDocsParseLog>()
                .eq(ApiDocsParseLog::getDocsConfigId, docsConfigId)
                .eq(ApiDocsParseLog::getParseTimestamp, parseTimestamp)
                .orderByAsc(ApiDocsParseLog::getCreateTimestamp));
        if (ListUtil.isNotBlank(list)) {
            for (ApiDocsParseLog log : list) {
                msgList.add(log.getLogMsg());
            }
        }
        return msgList;
    }

}
