package io.gitee.xuchenoak.limejapidocs.runner.runner;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.script.ScriptUtil;
import io.gitee.xuchenoak.limejapidocs.parser.basenode.AnnotationNode;
import io.gitee.xuchenoak.limejapidocs.parser.config.ParserConfig;
import io.gitee.xuchenoak.limejapidocs.parser.handler.ParserConfigHandler;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldInfo;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口文档配置控制类
 *
 * @author xuchenoak
 */
public abstract class DocsParserConfigAbstractHandler implements ParserConfigHandler {

    private ApiDocsConfig docsConfig;

    public DocsParserConfigAbstractHandler(ApiDocsConfig docsConfig) {
        this.docsConfig = docsConfig;
    }

    /**
     * 获取开始解析时间
     * @return
     */
    @Override
    public Date getParseTime() {
        return MsgUtil.getParseTime(docsConfig.getId());
    }

    /**
     * 获取解析全局配置
     * @return
     */
    @Override
    public ParserConfig getParserConfig() {
        if (ListUtil.isBlank(docsConfig.getJavaFilePaths())) {
            CusExc.e("源码路径不能为空");
        }
        ParserConfig parserConfig = null;
        for (String filePath : docsConfig.getJavaFilePaths()) {
            if (parserConfig == null) {
                parserConfig = ParserConfig.build(filePath);
            } else {
                parserConfig.addJavaFilePath(filePath);
            }
        }
        if (ListUtil.isNotBlank(docsConfig.getFilterPackages())) {
            for (String filterPackage : docsConfig.getFilterPackages()) {
                parserConfig.addFilterControllerPackage(filterPackage);
            }
        }
        if (ListUtil.isNotBlank(docsConfig.getFilterClassNames())) {
            for (String name : docsConfig.getFilterClassNames()) {
                parserConfig.addFilterControllerName(name);
            }
        }
        if (ListUtil.isNotBlank(docsConfig.getIgnoreClassNames())) {
            for (String name : docsConfig.getIgnoreClassNames()) {
                parserConfig.addIgnoreControllerName(name);
            }
        }
        return parserConfig;
    }

    /**
     * 参数验证注入
     * @param annotationNodeList
     * @param fieldInfo
     */
    @Override
    public void paramValidInjectHandle(List<AnnotationNode> annotationNodeList, FieldInfo fieldInfo) {
        String func = docsConfig.getParamValidFunc();
        if (StrUtil.isBlank(func)) {
            ParserConfigHandler.super.paramValidInjectHandle(annotationNodeList, fieldInfo);
            return;
        }
        Set<String> annotationNames = Optional.ofNullable(annotationNodeList).orElse(new ArrayList<>()).stream().map(AnnotationNode::getName).collect(Collectors.toSet());
        String valid = "";
        try {
            Object value = ScriptUtil.invoke(func, "valid", ScriptUtil.eval(JSONUtil.toJsonStr(annotationNames)), fieldInfo.getName(), fieldInfo.getComment());
            if (value != null) {
                valid = value.toString();
            }
        } catch (Exception e) {} finally {
            fieldInfo.setValidation(valid);
        }
    }

    /**
     * 参数默认值注入
     * @param annotationNodeList
     * @param fieldInfo
     */
    @Override
    public void paramDefaultValueInjectHandle(List<AnnotationNode> annotationNodeList, FieldInfo fieldInfo) {
        String func = docsConfig.getParamDefaultValueFunc();
        if (StrUtil.isBlank(func)) {
            ParserConfigHandler.super.paramDefaultValueInjectHandle(annotationNodeList, fieldInfo);
            return;
        }
        String valid = "";
        try {
            Object value = ScriptUtil.invoke(func, "defaultValue", fieldInfo.getType(), fieldInfo.getName(), fieldInfo.getComment());
            if (value != null) {
                valid = value.toString();
            }
        } catch (Exception e) {} finally {
            fieldInfo.setDefaultValue(valid);
        }
    }

}
