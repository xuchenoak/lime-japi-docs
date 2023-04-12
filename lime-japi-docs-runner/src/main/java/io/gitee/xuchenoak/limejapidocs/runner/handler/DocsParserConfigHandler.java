package io.gitee.xuchenoak.limejapidocs.runner.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.parser.bean.ControllerData;
import io.gitee.xuchenoak.limejapidocs.parser.config.ParserConfig;
import io.gitee.xuchenoak.limejapidocs.parser.exception.CustomException;
import io.gitee.xuchenoak.limejapidocs.parser.handler.ParserConfigHandler;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.bean.DocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.config.DocsParserConfig;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.ApiDocsControllerDataService;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.ApiDocsParseLogService;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 接口文档配置控制类
 * @author: xuchenoak
 * @create: 2022/06/04 20:17
 */
@Component
public class DocsParserConfigHandler implements ParserConfigHandler {

    @Autowired
    private ApiDocsControllerDataService apiDocsControllerDataService;

    @Autowired
    private DocsParserConfig docsParserConfig;

    @Autowired
    private ApiDocsParseLogService apiDocsParseLogService;

    /**
     * 获取开始解析时间
     * @return
     */
    @Override
    public Date getParseTime() {
        DateTime time = MsgUtil.getParseTime();
        System.out.println("\n\n获取时间：" + time.toString("yyyy-MM-dd HH:mm:ss") + "\n\n");
        return MsgUtil.getParseTime();
    }

    /**
     * 解析控制
     * @param controllerData Controller及其接口方法数据
     */
    @Override
    public void controllerDataHandle(ControllerData controllerData) {
        String msg = StrUtil.format("成功生成-{}：{}", controllerData.getSort(), controllerData.getComment());
        apiDocsParseLogService.addMsg(msg);
    }

    /**
     * 获取文档配置
     * @return
     */
    public DocsConfig getDocsConfig() {
        return new DocsConfig(docsParserConfig.getDocsName(), docsParserConfig.getVersion());
    }

    /**
     * 是否开启解析
     * @return
     */
    public boolean isSysStartParse() {
        return docsParserConfig.getSysStartParse();
    }

    /**
     * 获取解析全局配置
     * @return
     */
    @Override
    public ParserConfig getParserConfig() {
        if (ListUtil.isBlank(docsParserConfig.getJavaFilePaths())) {
            throw CustomException.instance("源码路径不能为空");
        }
        ParserConfig parserConfig = null;
        for (String filePath : docsParserConfig.getJavaFilePaths()) {
            if (parserConfig == null) {
                parserConfig = ParserConfig.build(filePath);
            } else {
                parserConfig.addJavaFilePath(filePath);
            }
        }
        if (ListUtil.isNotBlank(docsParserConfig.getFilterPackages())) {
            for (String filterPackage : docsParserConfig.getFilterPackages()) {
                parserConfig.addFilterControllerPackage(filterPackage);
            }
        }
        if (ListUtil.isNotBlank(docsParserConfig.getFilterClassNames())) {
            for (String name : docsParserConfig.getFilterClassNames()) {
                parserConfig.addFilterControllerName(name);
            }
        }
        if (ListUtil.isNotBlank(docsParserConfig.getIgnoreClassNames())) {
            for (String name : docsParserConfig.getIgnoreClassNames()) {
                parserConfig.addIgnoreControllerName(name);
            }
        }
        return parserConfig;
    }

    /**
     * 获取生成时间集
     * @return
     */
    public List<String> getCreateTimeList() {
        return apiDocsControllerDataService.getCreateTimeList();
    }

    /**
     * 获取所有接口数据
     * @param createTime 生成时间
     * @return
     */
    public List<ControllerData> getControllerDataList(String createTime) {
        return apiDocsControllerDataService.getControllerDataList(createTime);
    }

    /**
     * 获取单个Controller接口数据
     * @param controllerId
     * @return
     */
    public ControllerData getControllerData(String createTime, String controllerId) {
        return apiDocsControllerDataService.getControllerData(createTime, controllerId);
    }

    /**
     * 解析完成
     * @param controllerDataList
     */
    @Override
    public void parseFinishedHandle(List<ControllerData> controllerDataList) {
        apiDocsControllerDataService.saveControllerData(controllerDataList);
        String msg = StrUtil.format("生成完成！共生成了{}个接口目录", controllerDataList.size());
        apiDocsParseLogService.addMsg(msg);
    }


}
