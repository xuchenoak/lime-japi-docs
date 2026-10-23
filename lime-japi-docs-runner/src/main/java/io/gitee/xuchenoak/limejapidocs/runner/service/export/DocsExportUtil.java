package io.gitee.xuchenoak.limejapidocs.runner.service.export;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldDataNode;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldInfo;
import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 文档导出工具类
 *
 * @author xuchenoak
 **/
public final class DocsExportUtil {

    private DocsExportUtil() {
    }

    /**
     * 获取controller显示名称
     *
     * @param controller controller数据
     * @return controller显示名称
     */
    public static String controllerName(ApiDocsControllerData controller) {
        if (controller == null) {
            return "未命名目录";
        }
        return StrUtil.blankToDefault(controller.getComment(),
                StrUtil.blankToDefault(controller.getControllerFullName(),
                        StrUtil.blankToDefault(controller.getControllerId(), "未命名目录")));
    }

    /**
     * 获取接口显示名称
     *
     * @param interfaceData 接口数据
     * @return 接口显示名称
     */
    public static String interfaceName(InterfaceData interfaceData) {
        if (interfaceData == null) {
            return "未命名接口";
        }
        return StrUtil.blankToDefault(interfaceData.getComment(),
                StrUtil.blankToDefault(interfaceData.getMethodName(),
                        StrUtil.blankToDefault(interfaceData.getInterfaceId(), "未命名接口")));
    }

    /**
     * 生成安全的文件名称
     *
     * @param name 原始名称
     * @return 安全文件名称
     */
    public static String safeFileName(String name) {
        String result = StrUtil.blankToDefault(name, "api-docs").trim();
        result = result.replaceAll("[\\\\/:*?\"<>|;\\r\\n]", "_");
        result = result.replaceAll("\\s+", " ");
        if (result.length() > 80) {
            result = result.substring(0, 80);
        }
        return StrUtil.blankToDefault(result, "api-docs");
    }

    /**
     * 将接口声明的Content-Type规范化为OpenAPI的media type
     *
     * @param contentType 原始Content-Type
     * @param jsonDefault 为空时是否默认application/json
     * @return 规范化后的Content-Type
     */
    public static String normalizeContentType(String contentType, boolean jsonDefault) {
        String defaultType = jsonDefault ? "application/json" : "application/x-www-form-urlencoded";
        if (StrUtil.isBlank(contentType)) {
            return defaultType;
        }
        String result = contentType.trim();
        int index = result.indexOf(';');
        if (index > -1) {
            result = result.substring(0, index).trim();
        }
        index = result.indexOf(',');
        if (index > -1) {
            result = result.substring(0, index).trim();
        }
        return StrUtil.blankToDefault(result, defaultType);
    }

    /**
     * 判断字段是否必填
     *
     * @param fieldInfo 字段信息
     * @return 是否必填
     */
    public static boolean isRequired(FieldInfo fieldInfo) {
        if (fieldInfo == null) {
            return false;
        }
        String validation = StrUtil.blankToDefault(fieldInfo.getValidation(), "").toLowerCase(Locale.ROOT);
        return validation.contains("不能为空")
                || validation.contains("不能为null")
                || validation.contains("不能为 null")
                || validation.contains("必填")
                || validation.contains("notnull")
                || validation.contains("notblank")
                || validation.contains("notempty");
    }

    /**
     * 构建字段描述
     *
     * @param fieldInfo 字段信息
     * @return 字段描述
     */
    public static String buildDescription(FieldInfo fieldInfo) {
        if (fieldInfo == null) {
            return "";
        }
        String comment = StrUtil.blankToDefault(fieldInfo.getComment(), "").trim();
        String validation = StrUtil.blankToDefault(fieldInfo.getValidation(), "").trim();
        if (StrUtil.isBlank(comment)) {
            return validation;
        }
        if (StrUtil.isBlank(validation)) {
            return comment;
        }
        return StrUtil.format("{}（{}）", comment, validation);
    }

    /**
     * 根据字段名称查找字段
     *
     * @param fieldInfoList 字段列表
     * @param name          字段名称
     * @return 字段信息
     */
    public static FieldInfo findField(List<FieldInfo> fieldInfoList, String name) {
        if (fieldInfoList == null || StrUtil.isBlank(name)) {
            return null;
        }
        for (FieldInfo fieldInfo : fieldInfoList) {
            if (fieldInfo != null && name.equals(fieldInfo.getName())) {
                return fieldInfo;
            }
        }
        return null;
    }

    /**
     * 解析字段默认值
     *
     * @param defaultValue 默认值字符串
     * @param type         字段类型
     * @return 解析后的默认值
     */
    public static Object parseDefaultValue(String defaultValue, String type) {
        if (StrUtil.isBlank(defaultValue)) {
            return null;
        }
        String value = defaultValue.trim();
        try {
            return JSONUtil.parse(value);
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * 构建格式化JSON示例
     *
     * @param fieldDataNode 字段数据节点
     * @return 格式化JSON示例
     */
    public static String buildExample(FieldDataNode fieldDataNode) {
        Object example = parseExampleObject(fieldDataNode);
        if (example == null) {
            return null;
        }
        return JSONUtil.toJsonPrettyStr(example);
    }

    /**
     * 构建JSON示例对象
     *
     * @param fieldDataNode 字段数据节点
     * @return JSON示例对象
     */
    public static Object parseExampleObject(FieldDataNode fieldDataNode) {
        if (fieldDataNode == null) {
            return null;
        }
        String json = StringUtil.toFormatJsonStr(fieldDataNode, 0, 2, false, false, false, true, true);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        try {
            return JSONUtil.parse(json);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 数字补零
     *
     * @param value 数字
     * @return 两位数字符串
     */
    public static String twoDigit(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

    /**
     * 转义Markdown表格单元格
     *
     * @param value 原始内容
     * @return 转义后的内容
     */
    public static String escapeTableCell(String value) {
        if (StrUtil.isBlank(value)) {
            return "-";
        }
        return value.replace("|", "\\|")
                .replace("\r", "")
                .replace("\n", "<br>");
    }

    /**
     * 过滤空字符串集合
     *
     * @param list 原始集合
     * @return 过滤后的集合
     */
    public static List<String> filterBlank(List<String> list) {
        List<String> result = new ArrayList<>();
        if (list == null) {
            return result;
        }
        for (String item : list) {
            if (StrUtil.isNotBlank(item)) {
                result.add(item.trim());
            }
        }
        return result;
    }
}
