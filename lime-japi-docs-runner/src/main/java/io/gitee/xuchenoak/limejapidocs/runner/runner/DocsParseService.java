package io.gitee.xuchenoak.limejapidocs.runner.runner;

import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.parser.LimeJapiDocsParser;
import io.gitee.xuchenoak.limejapidocs.runner.handler.DocsParserConfigHandler;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.ApiDocsParseLogService;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 解析业务接口
 *
 * @author xuchenoak
 **/
@Service
public class DocsParseService {

    private static final Logger logger = LoggerFactory.getLogger(DocsParseService.class);

    @Autowired
    private DocsParserConfigHandler docsParserConfigHandler;

    @Autowired
    private ApiDocsParseLogService apiDocsParseLogService;

    @Async
    public void runParse() {
        try {
            logger.info("\n\n------ LimeJapiDocs 开始生成 ------\n");
            apiDocsParseLogService.addMsg(StrUtil.format("开始生成！时间版本：{}", MsgUtil.getParseTime().toString("yyyy-MM-dd HH:mm:ss")));
            LimeJapiDocsParser.build(docsParserConfigHandler);
            logger.info("\n\n------ LimeJapiDocs 生成完成 ------\n");
        } catch (Exception e) {
            logger.error("LimeJapiDocs 生成异常", e);
            apiDocsParseLogService.addMsg(StrUtil.format("生成异常！时间版本：{}", MsgUtil.getParseTime().toString("yyyy-MM-dd HH:mm:ss")));
        } finally {
            MsgUtil.statusParseOver();
        }
    }

}
