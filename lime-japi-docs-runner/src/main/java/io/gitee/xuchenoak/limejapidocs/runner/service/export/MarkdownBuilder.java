package io.gitee.xuchenoak.limejapidocs.runner.service.export;

import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldDataNode;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldInfo;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ExportScopeEnum;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Markdown文档构建器
 *
 * @author xuchenoak
 **/
@Component
public class MarkdownBuilder {

    /**
     * 构建Markdown文档
     *
     * @param data 导出数据
     * @return Markdown字符串
     */
    public String build(DocsExportData data) {
        StringBuilder markdown = new StringBuilder();
        String docsName = StrUtil.blankToDefault(data.getDocsConfig().getDocsName(), "接口文档");

        if (data.getScope() == ExportScopeEnum.PROJECT) {
            appendHeading(markdown, 1, docsName);
            appendMeta(markdown, data);
            appendCatalog(markdown, data);

            int controllerIndex = 1;
            for (DocsExportControllerData controllerData : data.getControllerList()) {
                ApiDocsControllerData controller = controllerData.getController();
                appendHeading(markdown, 2, DocsExportUtil.twoDigit(controllerIndex++) + " " + DocsExportUtil.controllerName(controller));
                appendControllerInfo(markdown, controller);

                int interfaceIndex = 1;
                for (InterfaceData interfaceData : controllerData.getInterfaceList()) {
                    appendInterface(markdown, interfaceData, 3, interfaceIndex++);
                }
            }
        } else if (data.getScope() == ExportScopeEnum.CONTROLLER) {
            ApiDocsControllerData controller = data.getControllerList().get(0).getController();
            appendHeading(markdown, 1, StrUtil.blankToDefault(data.getExportName(), DocsExportUtil.controllerName(controller)));
            appendMeta(markdown, data);
            appendControllerInfo(markdown, controller);

            int interfaceIndex = 1;
            for (InterfaceData interfaceData : data.getControllerList().get(0).getInterfaceList()) {
                appendInterface(markdown, interfaceData, 2, interfaceIndex++);
            }
        } else {
            DocsExportControllerData controllerData = data.getControllerList().get(0);
            InterfaceData interfaceData = controllerData.getInterfaceList().get(0);
            appendHeading(markdown, 1, StrUtil.blankToDefault(data.getExportName(), DocsExportUtil.interfaceName(interfaceData)));
            appendMeta(markdown, data);
            appendControllerInfo(markdown, controllerData.getController());
            appendInterfaceBody(markdown, interfaceData, 2);
        }
        return markdown.toString();
    }

    /**
     * 追加文档目录
     *
     * @param markdown Markdown内容
     * @param data     导出数据
     */
    private void appendCatalog(StringBuilder markdown, DocsExportData data) {
        appendHeading(markdown, 2, "目录");
        int index = 1;
        for (DocsExportControllerData controllerData : data.getControllerList()) {
            markdown.append(index++).append(". ").append(DocsExportUtil.controllerName(controllerData.getController())).append("\n");
        }
        markdown.append("\n");
    }

    /**
     * 追加文档元信息
     *
     * @param markdown Markdown内容
     * @param data     导出数据
     */
    private void appendMeta(StringBuilder markdown, DocsExportData data) {
        markdown.append("> 文档：").append(StrUtil.blankToDefault(data.getDocsConfig().getDocsName(), "接口文档"));
        if (StrUtil.isNotBlank(data.getDocsConfig().getDocsVersion())) {
            markdown.append(" | 版本：").append(data.getDocsConfig().getDocsVersion());
        }
        markdown.append(" | 生成时间：").append(data.getCreateTime()).append("\n\n");
    }

    /**
     * 追加controller信息
     *
     * @param markdown   Markdown内容
     * @param controller controller数据
     */
    private void appendControllerInfo(StringBuilder markdown, ApiDocsControllerData controller) {
        if (StrUtil.isNotBlank(controller.getControllerFullName())) {
            markdown.append("> Controller：`").append(controller.getControllerFullName()).append("`\n\n");
        }
    }

    /**
     * 追加接口内容
     *
     * @param markdown      Markdown内容
     * @param interfaceData 接口数据
     * @param titleLevel    标题级别
     * @param index         接口序号
     */
    private void appendInterface(StringBuilder markdown, InterfaceData interfaceData, int titleLevel, int index) {
        appendHeading(markdown, titleLevel,
                DocsExportUtil.twoDigit(index) + " " + DocsExportUtil.interfaceName(interfaceData));
        appendInterfaceBody(markdown, interfaceData, titleLevel + 1);
    }

    /**
     * 追加接口详情
     *
     * @param markdown      Markdown内容
     * @param interfaceData 接口数据
     * @param subTitleLevel 子标题级别
     */
    private void appendInterfaceBody(StringBuilder markdown, InterfaceData interfaceData, int subTitleLevel) {
        appendRequestInfo(markdown, interfaceData);
        appendParameterTable(markdown, interfaceData.getFormData(), subTitleLevel);
        appendExample(markdown, "请求体示例", interfaceData.getBodyData(), subTitleLevel);
        appendExample(markdown, "响应示例", interfaceData.getResData(), subTitleLevel);
    }

    /**
     * 追加请求方式与地址
     *
     * @param markdown      Markdown内容
     * @param interfaceData 接口数据
     */
    private void appendRequestInfo(StringBuilder markdown, InterfaceData interfaceData) {
        List<String> methodList = DocsExportUtil.filterBlank(interfaceData.getRequestTypeList());
        List<String> uriList = DocsExportUtil.filterBlank(interfaceData.getUriList());
        if (!methodList.isEmpty() && !uriList.isEmpty()) {
            markdown.append("**请求方式与地址**\n\n");
            markdown.append("| 请求方式 | 请求地址 |\n");
            markdown.append("| --- | --- |\n");
            for (String method : methodList) {
                for (String uri : uriList) {
                    markdown.append("| ")
                            .append(DocsExportUtil.escapeTableCell(method))
                            .append(" | `")
                            .append(DocsExportUtil.escapeTableCell(uri))
                            .append("` |\n");
                }
            }
            markdown.append("\n");
        }
        if (StrUtil.isNotBlank(interfaceData.getRequestContentType())) {
            markdown.append("**Content-Type**：`")
                    .append(interfaceData.getRequestContentType())
                    .append("`\n\n");
        }
    }

    /**
     * 追加请求参数表格
     *
     * @param markdown      Markdown内容
     * @param fieldInfoList 字段列表
     * @param titleLevel    标题级别
     */
    private void appendParameterTable(StringBuilder markdown, List<FieldInfo> fieldInfoList, int titleLevel) {
        if (fieldInfoList == null || fieldInfoList.isEmpty()) {
            return;
        }
        appendHeading(markdown, titleLevel, "请求参数");
        markdown.append("| 参数名 | 类型 | 必填 | 说明 | 校验 | 默认值 |\n");
        markdown.append("| --- | --- | --- | --- | --- | --- |\n");
        for (FieldInfo fieldInfo : fieldInfoList) {
            if (fieldInfo == null || StrUtil.isBlank(fieldInfo.getName())) {
                continue;
            }
            markdown.append("| ")
                    .append(DocsExportUtil.escapeTableCell(fieldInfo.getName()))
                    .append(" | ")
                    .append(DocsExportUtil.escapeTableCell(fieldInfo.getType()))
                    .append(" | ")
                    .append(DocsExportUtil.isRequired(fieldInfo) ? "是" : "否")
                    .append(" | ")
                    .append(DocsExportUtil.escapeTableCell(fieldInfo.getComment()))
                    .append(" | ")
                    .append(DocsExportUtil.escapeTableCell(fieldInfo.getValidation()))
                    .append(" | ")
                    .append(DocsExportUtil.escapeTableCell(fieldInfo.getDefaultValue()))
                    .append(" |\n");
        }
        markdown.append("\n");
    }

    /**
     * 追加JSON示例
     *
     * @param markdown      Markdown内容
     * @param title         示例标题
     * @param fieldDataNode 字段数据节点
     * @param titleLevel    标题级别
     */
    private void appendExample(StringBuilder markdown, String title,
                               FieldDataNode fieldDataNode,
                               int titleLevel) {
        String example = DocsExportUtil.buildExample(fieldDataNode);
        if (StrUtil.isBlank(example)) {
            return;
        }
        appendHeading(markdown, titleLevel, title);
        markdown.append("```json\n").append(example).append("\n```\n\n");
    }

    /**
     * 追加标题
     *
     * @param markdown Markdown内容
     * @param level    标题级别
     * @param title    标题内容
     */
    private void appendHeading(StringBuilder markdown, int level, String title) {
        int safeLevel = Math.max(1, Math.min(level, 6));
        markdown.append("#".repeat(safeLevel)).append(" ").append(title).append("\n\n");
    }
}
