package io.gitee.xuchenoak.limejapidocs.runner.runner;


import io.gitee.xuchenoak.limejapidocs.runner.handler.DocsParserConfigHandler;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动接口解析
 *
 * @author: xuchenoak
 * @create: 2022-07-16 0:21
 **/
@Component
public class DocsParserRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DocsParserRunner.class);

    @Autowired
    private DocsParserConfigHandler docsParserConfigHandler;

    @Autowired
    private DocsParseService docsParseService;

    @Override
    public void run(ApplicationArguments args) {
        if (docsParserConfigHandler == null) {
            logger.info("LimeJapiDocs 解析未获取到有效DocsParserConfigHandler");
            return;
        }
        if (!docsParserConfigHandler.isSysStartParse()) {
            logger.info("LimeJapiDocs 启动时解析未开启");
            return;
        }
        MsgUtil.statusParseRun();
        docsParseService.runParse();
    }

}
