package io.gitee.xuchenoak.limejapidocs.runner.service.export;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldDataNode;
import io.gitee.xuchenoak.limejapidocs.parser.parsendoe.FieldInfo;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ExportScopeEnum;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OpenAPI文档构建器
 *
 * @author xuchenoak
 **/
@Slf4j
@Component
public class OpenApiBuilder {

    /**
     * OpenAPI规范版本
     */
    private static final String OPENAPI_VERSION = "3.0.3";

    /**
     * 路径变量正则
     */
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([^}:]+)(?::[^}]*)?\\}");

    /**
     * 查询参数请求方式
     */
    private static final Set<String> QUERY_METHODS = Set.of("get", "head", "delete", "options", "trace");

    /**
     * 请求体请求方式
     */
    private static final Set<String> BODY_METHODS = Set.of("post", "put", "patch");

    /**
     * 支持的请求方式
     */
    private static final Set<String> SUPPORT_METHODS = Set.of("get", "post", "put", "delete", "patch", "head", "options", "trace");

    /**
     * 构建OpenAPI文档
     *
     * @param data 导出数据
     * @return OpenAPI JSON字符串
     */
    public String build(DocsExportData data) {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("openapi", OPENAPI_VERSION);
        root.put("info", buildInfo(data));
        root.put("tags", buildTags(data));
        root.put("paths", buildPaths(data));
        return JSONUtil.toJsonPrettyStr(root);
    }

    /**
     * 构建info节点
     *
     * @param data 导出数据
     * @return info节点
     */
    private Map<String, Object> buildInfo(DocsExportData data) {
        String title = StrUtil.blankToDefault(data.getDocsConfig().getDocsName(), "接口文档");
        if (data.getScope() != ExportScopeEnum.PROJECT && StrUtil.isNotBlank(data.getExportName())) {
            title = title + " - " + data.getExportName();
        }
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("title", title);
        info.put("version", StrUtil.blankToDefault(data.getDocsConfig().getDocsVersion(), "1.0.0"));
        info.put("description", StrUtil.format("由 lime-japi-docs 导出，生成时间：{}", data.getCreateTime()));
        return info;
    }

    /**
     * 构建tags节点
     *
     * @param data 导出数据
     * @return tags节点
     */
    private List<Map<String, Object>> buildTags(DocsExportData data) {
        List<Map<String, Object>> tags = new ArrayList<>();
        Set<String> tagNames = new LinkedHashSet<>();
        for (DocsExportControllerData controllerData : data.getControllerList()) {
            ApiDocsControllerData controller = controllerData.getController();
            String tagName = DocsExportUtil.controllerName(controller);
            if (!tagNames.add(tagName)) {
                continue;
            }
            Map<String, Object> tag = new LinkedHashMap<>();
            tag.put("name", tagName);
            if (StrUtil.isNotBlank(controller.getControllerFullName())) {
                tag.put("description", controller.getControllerFullName());
            }
            tags.add(tag);
        }
        return tags;
    }

    /**
     * 构建paths节点
     *
     * @param data 导出数据
     * @return paths节点
     */
    private Map<String, Object> buildPaths(DocsExportData data) {
        Map<String, Object> paths = new LinkedHashMap<>();
        Set<String> operationIds = new HashSet<>();
        for (DocsExportControllerData controllerData : data.getControllerList()) {
            ApiDocsControllerData controller = controllerData.getController();
            String tagName = DocsExportUtil.controllerName(controller);
            for (InterfaceData interfaceData : controllerData.getInterfaceList()) {
                if (interfaceData == null) {
                    continue;
                }
                List<String> uriList = DocsExportUtil.filterBlank(interfaceData.getUriList());
                List<String> methodList = DocsExportUtil.filterBlank(interfaceData.getRequestTypeList());
                if (uriList.isEmpty() || methodList.isEmpty()) {
                    continue;
                }
                for (String uri : uriList) {
                    String path = normalizePath(uri);
                    Map<String, Object> pathItem = getPathItem(paths, path);
                    for (String method : methodList) {
                        String methodKey = method.toLowerCase(Locale.ROOT);
                        if (!SUPPORT_METHODS.contains(methodKey)) {
                            log.warn("导出文档时跳过不支持的请求方式：{}，接口：{}", method, DocsExportUtil.interfaceName(interfaceData));
                            continue;
                        }
                        if (pathItem.containsKey(methodKey)) {
                            log.warn("导出文档时发现重复的接口路径与方法，已保留第一个：{} {}", methodKey.toUpperCase(Locale.ROOT), path);
                            continue;
                        }
                        String operationId = buildOperationId(interfaceData, methodKey, path);
                        while (!operationIds.add(operationId)) {
                            operationId = operationId + "_" + operationIds.size();
                        }
                        pathItem.put(methodKey, buildOperation(controller, interfaceData, methodKey, uri, path, tagName, operationId));
                    }
                }
            }
        }
        return paths;
    }

    /**
     * 获取或创建path节点
     *
     * @param paths 所有path
     * @param path  当前path
     * @return path节点
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getPathItem(Map<String, Object> paths, String path) {
        Object pathItem = paths.get(path);
        if (pathItem instanceof Map) {
            return (Map<String, Object>) pathItem;
        }
        Map<String, Object> newPathItem = new LinkedHashMap<>();
        paths.put(path, newPathItem);
        return newPathItem;
    }

    /**
     * 构建单个接口操作节点
     *
     * @param controller    controller数据
     * @param interfaceData 接口数据
     * @param method        请求方式
     * @param uri           原始请求地址
     * @param path          规范化后的请求地址
     * @param tagName       controller标签
     * @param operationId   操作标识
     * @return 操作节点
     */
    private Map<String, Object> buildOperation(ApiDocsControllerData controller,
                                               InterfaceData interfaceData, String method, String uri,
                                               String path, String tagName, String operationId) {
        Map<String, Object> operation = new LinkedHashMap<>();
        operation.put("tags", Collections.singletonList(tagName));
        operation.put("summary", StrUtil.blankToDefault(interfaceData.getComment(),
                StrUtil.blankToDefault(interfaceData.getMethodName(), method.toUpperCase(Locale.ROOT) + " " + path)));
        operation.put("operationId", operationId);
        String description = buildOperationDescription(controller, interfaceData);
        if (StrUtil.isNotBlank(description)) {
            operation.put("description", description);
        }
        List<Map<String, Object>> parameters = buildParameters(interfaceData, method, uri);
        if (!parameters.isEmpty()) {
            operation.put("parameters", parameters);
        }
        Map<String, Object> requestBody = buildRequestBody(interfaceData, method);
        if (requestBody != null) {
            operation.put("requestBody", requestBody);
        }
        operation.put("responses", buildResponses(interfaceData));
        return operation;
    }

    /**
     * 构建操作描述
     *
     * @param controller    controller数据
     * @param interfaceData 接口数据
     * @return 操作描述
     */
    private String buildOperationDescription(ApiDocsControllerData controller, InterfaceData interfaceData) {
        List<String> parts = new ArrayList<>();
        if (StrUtil.isNotBlank(controller.getControllerFullName())) {
            parts.add("Controller：" + controller.getControllerFullName());
        }
        if (StrUtil.isNotBlank(interfaceData.getMethodName())) {
            parts.add("方法：" + interfaceData.getMethodName());
        }
        return String.join("\n", parts);
    }

    /**
     * 构建操作标识
     *
     * @param interfaceData 接口数据
     * @param method        请求方式
     * @param path          请求地址
     * @return 操作标识
     */
    private String buildOperationId(InterfaceData interfaceData, String method, String path) {
        String base = StrUtil.blankToDefault(interfaceData.getInterfaceId(), "api");
        String operationId = base + "_" + method + "_" + Integer.toHexString(path.hashCode());
        return operationId.replaceAll("[^A-Za-z0-9_.-]", "_");
    }

    /**
     * 构建parameters节点
     *
     * @param interfaceData 接口数据
     * @param method        请求方式
     * @param uri           原始请求地址
     * @return parameters节点
     */
    private List<Map<String, Object>> buildParameters(InterfaceData interfaceData, String method, String uri) {
        List<Map<String, Object>> parameters = new ArrayList<>();
        Set<String> parameterKeys = new HashSet<>();

        // 路径参数
        for (String name : extractPathParamNames(uri)) {
            String key = "path:" + name;
            if (!parameterKeys.add(key)) {
                continue;
            }
            FieldInfo fieldInfo = DocsExportUtil.findField(interfaceData.getFormData(), name);
            Map<String, Object> parameter = new LinkedHashMap<>();
            parameter.put("name", name);
            parameter.put("in", "path");
            parameter.put("required", true);
            String description = DocsExportUtil.buildDescription(fieldInfo);
            if (StrUtil.isNotBlank(description)) {
                parameter.put("description", description);
            }
            parameter.put("schema", fieldInfo == null ? newSchema("string") : buildPropertySchema(fieldInfo));
            parameters.add(parameter);
        }

        // 查询参数
        if (QUERY_METHODS.contains(method) && interfaceData.getFormData() != null) {
            for (FieldInfo fieldInfo : interfaceData.getFormData()) {
                if (fieldInfo == null || StrUtil.isBlank(fieldInfo.getName())) {
                    continue;
                }
                String key = "query:" + fieldInfo.getName();
                if (!parameterKeys.add(key)) {
                    continue;
                }
                Map<String, Object> parameter = new LinkedHashMap<>();
                parameter.put("name", fieldInfo.getName());
                parameter.put("in", "query");
                parameter.put("required", DocsExportUtil.isRequired(fieldInfo));
                String description = DocsExportUtil.buildDescription(fieldInfo);
                if (StrUtil.isNotBlank(description)) {
                    parameter.put("description", description);
                }
                parameter.put("schema", buildPropertySchema(fieldInfo));
                Object defaultValue = DocsExportUtil.parseDefaultValue(fieldInfo.getDefaultValue(), fieldInfo.getType());
                if (defaultValue != null) {
                    parameter.put("example", defaultValue);
                }
                parameters.add(parameter);
            }
        }
        return parameters;
    }

    /**
     * 构建requestBody节点
     *
     * @param interfaceData 接口数据
     * @param method        请求方式
     * @return requestBody节点
     */
    private Map<String, Object> buildRequestBody(InterfaceData interfaceData, String method) {
        if (!BODY_METHODS.contains(method)) {
            return null;
        }
        FieldDataNode bodyData = interfaceData.getBodyData();
        if (bodyData != null) {
            String contentType = DocsExportUtil.normalizeContentType(interfaceData.getRequestContentType(), true);
            Map<String, Object> media = new LinkedHashMap<>();
            media.put("schema", ensureSchema(schemaFromNode(bodyData, null)));
            Object example = DocsExportUtil.parseExampleObject(bodyData);
            if (example != null) {
                media.put("example", example);
            }
            return buildRequestBodyContent(contentType, media);
        }
        List<FieldInfo> fieldInfoList = filterFields(interfaceData.getFormData());
        if (fieldInfoList.isEmpty()) {
            return null;
        }
        String contentType = DocsExportUtil.normalizeContentType(interfaceData.getRequestContentType(), false);
        Map<String, Object> media = new LinkedHashMap<>();
        media.put("schema", buildObjectSchema(fieldInfoList));
        return buildRequestBodyContent(contentType, media);
    }

    /**
     * 构建requestBody内容
     *
     * @param contentType Content-Type
     * @param media       media节点
     * @return requestBody节点
     */
    private Map<String, Object> buildRequestBodyContent(String contentType, Map<String, Object> media) {
        Map<String, Object> content = new LinkedHashMap<>();
        content.put(contentType, media);
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("required", true);
        requestBody.put("content", content);
        return requestBody;
    }

    /**
     * 构建responses节点
     *
     * @param interfaceData 接口数据
     * @return responses节点
     */
    private Map<String, Object> buildResponses(InterfaceData interfaceData) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("description", "成功");
        FieldDataNode resData = interfaceData.getResData();
        if (resData != null) {
            Map<String, Object> media = new LinkedHashMap<>();
            media.put("schema", ensureSchema(schemaFromNode(resData, null)));
            Object example = DocsExportUtil.parseExampleObject(resData);
            if (example != null) {
                media.put("example", example);
            }
            Map<String, Object> content = new LinkedHashMap<>();
            content.put("application/json", media);
            response.put("content", content);
        }
        Map<String, Object> responses = new LinkedHashMap<>();
        responses.put("200", response);
        return responses;
    }

    /**
     * 构建对象字段schema
     *
     * @param fieldInfoList 字段列表
     * @return 对象schema
     */
    private Map<String, Object> buildObjectSchema(List<FieldInfo> fieldInfoList) {
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();
        for (FieldInfo fieldInfo : fieldInfoList) {
            properties.put(fieldInfo.getName(), buildPropertySchema(fieldInfo));
            if (DocsExportUtil.isRequired(fieldInfo)) {
                required.add(fieldInfo.getName());
            }
        }
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        if (!required.isEmpty()) {
            schema.put("required", required);
        }
        return schema;
    }

    /**
     * 构建字段schema
     *
     * @param fieldInfo 字段信息
     * @return 字段schema
     */
    private Map<String, Object> buildPropertySchema(FieldInfo fieldInfo) {
        Map<String, Object> schema = ensureSchema(schemaFromNode(fieldInfo.getValueFieldData(), fieldInfo.getType()));
        String description = DocsExportUtil.buildDescription(fieldInfo);
        if (StrUtil.isNotBlank(description)) {
            schema.put("description", description);
        }
        Object defaultValue = DocsExportUtil.parseDefaultValue(fieldInfo.getDefaultValue(), fieldInfo.getType());
        if (defaultValue != null) {
            schema.put("default", defaultValue);
        }
        return schema;
    }

    /**
     * 将字段数据节点转换为schema
     *
     * @param fieldDataNode 字段数据节点
     * @param fallbackType  兜底类型
     * @return schema
     */
    private Map<String, Object> schemaFromNode(FieldDataNode fieldDataNode, String fallbackType) {
        if (fieldDataNode == null) {
            return schemaFromType(fallbackType);
        }
        if (fieldDataNode.isArray()) {
            Map<String, Object> schema = newSchema("array");
            schema.put("items", ensureSchema(schemaFromNode(fieldDataNode.getChildFieldData(), null)));
            return schema;
        }
        if (fieldDataNode.isLastValue()) {
            return schemaFromType(StrUtil.blankToDefault(fieldDataNode.getLastValueType(), fallbackType));
        }
        if ((fieldDataNode.getFieldInfoList() == null || fieldDataNode.getFieldInfoList().isEmpty())
                && StrUtil.isNotBlank(fallbackType)) {
            return schemaFromType(fallbackType);
        }
        Map<String, Object> schema = newSchema("object");
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();
        if (fieldDataNode.getFieldInfoList() != null) {
            for (FieldInfo fieldInfo : fieldDataNode.getFieldInfoList()) {
                if (fieldInfo == null || StrUtil.isBlank(fieldInfo.getName())) {
                    continue;
                }
                properties.put(fieldInfo.getName(), buildPropertySchema(fieldInfo));
                if (DocsExportUtil.isRequired(fieldInfo)) {
                    required.add(fieldInfo.getName());
                }
            }
        }
        if (!properties.isEmpty()) {
            schema.put("properties", properties);
        }
        if (!required.isEmpty()) {
            schema.put("required", required);
        }
        return schema;
    }

    /**
     * 根据类型构建schema
     *
     * @param type 字段类型
     * @return schema
     */
    private Map<String, Object> schemaFromType(String type) {
        if (StrUtil.isBlank(type)) {
            return new LinkedHashMap<>();
        }
        String typeName = type.trim();
        String lowerType = typeName.toLowerCase(Locale.ROOT);
        int genericIndex = lowerType.indexOf('<');
        String genericName = genericIndex > -1 ? lowerType.substring(0, genericIndex) : lowerType;

        // 泛型集合
        if (genericName.endsWith("list") || genericName.endsWith("set")
                || genericName.endsWith("collection") || genericName.endsWith("arraylist")
                || genericName.endsWith("linkedlist")) {
            Map<String, Object> schema = newSchema("array");
            int start = typeName.indexOf('<');
            int end = typeName.lastIndexOf('>');
            String innerType = start > -1 && end > start ? typeName.substring(start + 1, end) : null;
            schema.put("items", ensureSchema(schemaFromType(innerType)));
            return schema;
        }
        if (lowerType.endsWith("[]")) {
            Map<String, Object> schema = newSchema("array");
            schema.put("items", ensureSchema(schemaFromType(typeName.substring(0, typeName.length() - 2))));
            return schema;
        }
        if (genericName.endsWith("map")) {
            return newSchema("object");
        }

        String simpleType = lowerType;
        int dotIndex = simpleType.lastIndexOf('.');
        if (dotIndex > -1) {
            simpleType = simpleType.substring(dotIndex + 1);
        }

        if (simpleType.equals("string") || simpleType.equals("char") || simpleType.equals("character")
                || simpleType.equals("uuid") || simpleType.equals("charset")
                || simpleType.equals("currency") || simpleType.equals("locale")
                || simpleType.equals("inetaddress") || simpleType.equals("duration")
                || simpleType.equals("period") || simpleType.equals("year")
                || simpleType.equals("yearmonth") || simpleType.equals("monthday")) {
            return newSchema("string");
        }
        if (simpleType.equals("localdate") || simpleType.equals("date") || simpleType.equals("sqldate")) {
            Map<String, Object> schema = newSchema("string");
            schema.put("format", "date");
            return schema;
        }
        if (simpleType.equals("localdatetime") || simpleType.equals("instant")
                || simpleType.equals("zoneddatetime") || simpleType.equals("offsetdatetime")
                || simpleType.equals("timestamp")) {
            Map<String, Object> schema = newSchema("string");
            schema.put("format", "date-time");
            return schema;
        }
        if (simpleType.equals("localtime") || simpleType.equals("offsettime") || simpleType.equals("time")
                || simpleType.equals("sqltime")) {
            Map<String, Object> schema = newSchema("string");
            schema.put("format", "time");
            return schema;
        }
        if (simpleType.equals("int") || simpleType.equals("integer") || simpleType.equals("short")
                || simpleType.equals("byte")) {
            Map<String, Object> schema = newSchema("integer");
            schema.put("format", "int32");
            return schema;
        }
        if (simpleType.equals("long") || simpleType.equals("biginteger")) {
            Map<String, Object> schema = newSchema("integer");
            schema.put("format", "int64");
            return schema;
        }
        if (simpleType.equals("double") || simpleType.equals("float") || simpleType.equals("bigdecimal")
                || simpleType.equals("number")) {
            Map<String, Object> schema = newSchema("number");
            schema.put("format", simpleType.equals("float") ? "float" : "double");
            return schema;
        }
        if (simpleType.equals("boolean") || simpleType.equals("bool")) {
            return newSchema("boolean");
        }
        if (simpleType.equals("object")) {
            return newSchema("object");
        }
        return new LinkedHashMap<>();
    }

    /**
     * 确保schema不为空
     *
     * @param schema 原始schema
     * @return 兜底schema
     */
    private Map<String, Object> ensureSchema(Map<String, Object> schema) {
        if (schema == null || schema.isEmpty()) {
            return newSchema("string");
        }
        return schema;
    }

    /**
     * 新建schema
     *
     * @param type 类型
     * @return schema
     */
    private Map<String, Object> newSchema(String type) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", type);
        return schema;
    }

    /**
     * 过滤无效字段
     *
     * @param fieldInfoList 字段列表
     * @return 有效字段列表
     */
    private List<FieldInfo> filterFields(List<FieldInfo> fieldInfoList) {
        List<FieldInfo> result = new ArrayList<>();
        if (fieldInfoList == null) {
            return result;
        }
        for (FieldInfo fieldInfo : fieldInfoList) {
            if (fieldInfo != null && StrUtil.isNotBlank(fieldInfo.getName())) {
                result.add(fieldInfo);
            }
        }
        return result;
    }

    /**
     * 提取路径变量名称
     *
     * @param uri 请求地址
     * @return 路径变量名称集合
     */
    private List<String> extractPathParamNames(String uri) {
        List<String> names = new ArrayList<>();
        if (StrUtil.isBlank(uri)) {
            return names;
        }
        Matcher matcher = PATH_VARIABLE_PATTERN.matcher(uri);
        while (matcher.find()) {
            String name = matcher.group(1).trim();
            if (!names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    /**
     * 规范化请求地址
     *
     * @param uri 原始请求地址
     * @return 规范化后的请求地址
     */
    private String normalizePath(String uri) {
        String path = uri == null ? "" : uri.trim();
        int queryIndex = path.indexOf('?');
        if (queryIndex > -1) {
            path = path.substring(0, queryIndex);
        }
        path = PATH_VARIABLE_PATTERN.matcher(path).replaceAll("{$1}");
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }
}
