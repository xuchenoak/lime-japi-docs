package io.gitee.xuchenoak.limejapidocs.runner.runner;

import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.parser.LimeJapiDocsParser;
import io.gitee.xuchenoak.limejapidocs.parser.bean.ControllerData;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsControllerDataService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsParseLogService;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 解析业务接口
 *
 * @author xuchenoak
 **/
@Service
public class DocsParseService {

    private static final Logger logger = LoggerFactory.getLogger(DocsParseService.class);

    @Resource
    private ApiDocsControllerDataService apiDocsControllerDataService;

    @Resource
    private ApiDocsParseLogService apiDocsParseLogService;

    /**
     * 异步执行解析
     *
     * @param docsConfig
     */
    @Async
    public void runParseAsync(ApiDocsConfig docsConfig) {
        runParse(docsConfig);
    }

    /**
     * 执行解析
     *
     * @param docsConfig
     */
    public void runParse(ApiDocsConfig docsConfig) {
        Long docsConfigId = docsConfig.getId();
        try {
            logger.info("\n\n------ LimeJapiDocs 开始生成 ------\n");
            apiDocsParseLogService.addMsg(docsConfigId, StrUtil.format("开始生成！时间版本：{}", MsgUtil.getParseTime(docsConfigId).toString("yyyy-MM-dd HH:mm:ss")));

            // 调用解析方法
            LimeJapiDocsParser.build(new DocsParserConfigAbstractHandler(docsConfig) {
                @Override
                public void controllerDataHandle(ControllerData controllerData) {
                    String msg = StrUtil.format("成功生成-{}：{}", controllerData.getSort(), controllerData.getComment());
                    apiDocsParseLogService.addMsg(docsConfigId, msg);
                }

                @Override
                public void parseFinishedHandle(List<ControllerData> controllerDataList) {
                    apiDocsControllerDataService.saveControllerData(docsConfigId, controllerDataList);
                    String msg = StrUtil.format("生成完成！共生成了{}个接口目录", controllerDataList.size());
                    apiDocsParseLogService.addMsg(docsConfigId, msg);
                }

            });

            logger.info("\n\n------ LimeJapiDocs 生成完成 ------\n");

        } catch (Exception e) {
            logger.error("LimeJapiDocs 生成异常", e);
            apiDocsParseLogService.addMsg(docsConfigId, StrUtil.format("生成异常！时间版本：{}", MsgUtil.getParseTime(docsConfigId).toString("yyyy-MM-dd HH:mm:ss")));
        } finally {
            MsgUtil.statusParseOver(docsConfigId);
        }
    }

}
