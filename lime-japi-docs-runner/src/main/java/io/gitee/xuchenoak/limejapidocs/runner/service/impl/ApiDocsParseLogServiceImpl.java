package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsParseLog;
import io.gitee.xuchenoak.limejapidocs.runner.mapper.ApiDocsParseLogMapper;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.ApiDocsParseLogService;
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
public class ApiDocsParseLogServiceImpl extends ServiceImpl<ApiDocsParseLogMapper, ApiDocsParseLog> implements ApiDocsParseLogService {

    /**
     * 添加消息
     * @param msg 消息内容
     */
    @Override
    public void addMsg(String msg) {
        msg = StrUtil.format("[{}]  {}", DateUtil.date().toString("yyyy-MM-dd HH:mm:ss"), msg);
        save(new ApiDocsParseLog(0L, MsgUtil.getParseTimestamp(), msg, System.currentTimeMillis()));
    }

    /**
     * 获取消息内容
     * @param parseTimestamp 解析时间戳
     * @return
     */
    @Override
    public List<String> listMsg(Long parseTimestamp) {
        List<String> msgList = new ArrayList<>();
        List<ApiDocsParseLog> list = list(new LambdaQueryWrapper<ApiDocsParseLog>()
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
