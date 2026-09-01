package io.gitee.xuchenoak.limejapidocs.runner.runner;


import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 启动接口解析
 *
 * @author xuchenoak
 **/
@Component
public class DocsParserRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DocsParserRunner.class);

    @Resource
    private ApiDocsConfigService apiDocsConfigService;

    @Resource
    private DocsParseService docsParseService;

    @Override
    public void run(ApplicationArguments args) {
        List<ApiDocsConfig> docsConfigs = apiDocsConfigService.list();
        if (ListUtils.isNotBlank(docsConfigs)) {
            for (ApiDocsConfig docsConfig : docsConfigs) {
                if (ListUtils.isBlank(docsConfig.getJavaFilePaths()) || !docsConfig.hasSysStartParse()) {
                    continue;
                }
                try {
                    MsgUtil.startParse(docsConfig.getId());
                    docsParseService.runParse(docsConfig);
                } catch (Exception e) {
                    logger.info(StrUtil.format("解析文档 docsConfigId={} 异常", docsConfig.getId()), e);
                }
            }
        }

    }

}
