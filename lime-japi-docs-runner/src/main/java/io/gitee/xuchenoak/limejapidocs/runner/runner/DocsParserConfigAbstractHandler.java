package io.gitee.xuchenoak.limejapidocs.runner.runner;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.parser.basenode.AnnotationNode;
import io.gitee.xuchenoak.limejapidocs.parser.config.ParserConfig;
import io.gitee.xuchenoak.limejapidocs.parser.handler.ParserConfigHandler;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldInfo;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.StringListTypeHandler;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.util.MsgUtil;
import io.gitee.xuchenoak.limejapidocs.runner.util.ScriptCallbackSession;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口文档配置控制类
 *
 * @author xuchenoak
 */
public abstract class DocsParserConfigAbstractHandler implements ParserConfigHandler {

    private ApiDocsConfig docsConfig;

    private final ScriptCallbackSession scriptCallbackSession = new ScriptCallbackSession();

    public DocsParserConfigAbstractHandler(ApiDocsConfig docsConfig) {
        this.docsConfig = docsConfig;
    }

    /**
     * 关闭JS回调会话，释放GraalVM堆外内存（解析结束后由调用方执行）
     */
    public void closeScriptCallbackSession() {
        scriptCallbackSession.close();
    }

    /**
     * 获取开始解析时间
     *
     * @return
     */
    @Override
    public Date getParseTime() {
        return MsgUtil.getParseTime(docsConfig.getId());
    }

    /**
     * 获取解析全局配置
     *
     * @return
     */
    @Override
    public ParserConfig getParserConfig() {
        List<String> javaFilePaths = StringListTypeHandler.parseToList(docsConfig.getJavaFilePaths());
        if (ListUtil.isBlank(javaFilePaths)) {
            CusExc.e("源码路径不能为空");
        }
        ParserConfig parserConfig = null;
        for (String filePath : javaFilePaths) {
            if (parserConfig == null) {
                parserConfig = ParserConfig.build(filePath);
            } else {
                parserConfig.addJavaFilePath(filePath);
            }
        }
        List<String> filterPackages = StringListTypeHandler.parseToList(docsConfig.getFilterPackages());
        if (ListUtil.isNotBlank(filterPackages)) {
            for (String filterPackage : filterPackages) {
                parserConfig.addFilterControllerPackage(filterPackage);
            }
        }

        List<String> filterClassNames = StringListTypeHandler.parseToList(docsConfig.getFilterClassNames());
        if (ListUtil.isNotBlank(filterClassNames)) {
            for (String name : filterClassNames) {
                parserConfig.addFilterControllerName(name);
            }
        }
        List<String> ignoreClassNames = StringListTypeHandler.parseToList(docsConfig.getIgnoreClassNames());
        if (ListUtil.isNotBlank(ignoreClassNames)) {
            for (String name : ignoreClassNames) {
                parserConfig.addIgnoreControllerName(name);
            }
        }
        parserConfig.addLastValueTypeFullName(

                // java.time 时间系列
                "java.time.Instant",
                "java.time.LocalDate",
                "java.time.LocalTime",
                "java.time.LocalDateTime",
                "java.time.ZonedDateTime",
                "java.time.OffsetDateTime",
                "java.time.OffsetTime",
                "java.time.Duration",
                "java.time.Period",
                "java.time.Year",
                "java.time.YearMonth",
                "java.time.MonthDay",

                // 其他JDK原子属性类型
                "java.util.UUID",
                "java.net.InetAddress",
                "java.nio.charset.Charset",
                "java.util.Currency",
                "java.util.Locale",

                // 旧时间类（业务上作为单一值，注意本身是可变类）
                "java.util.Date",
                "java.sql.Timestamp",
                "java.sql.Date",
                "java.sql.Time"
        );
        return parserConfig;
    }

    /**
     * 参数验证注入
     *
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
            Object value = scriptCallbackSession.invoke(func, "valid", JSONUtil.toJsonStr(annotationNames), fieldInfo.getName(), fieldInfo.getComment());
            if (value != null) {
                valid = value.toString();
            }
        } catch (Exception e) {
        } finally {
            fieldInfo.setValidation(valid);
        }
    }

    /**
     * 参数默认值注入
     *
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
            Object value = scriptCallbackSession.invoke(func, "defaultValue", fieldInfo.getType(), fieldInfo.getName(), fieldInfo.getComment());
            if (value != null) {
                valid = value.toString();
            }
        } catch (Exception e) {
        } finally {
            fieldInfo.setDefaultValue(valid);
        }
    }

    @Override
    public boolean isParseControllerFirstParent() {
        return true;
    }
}
