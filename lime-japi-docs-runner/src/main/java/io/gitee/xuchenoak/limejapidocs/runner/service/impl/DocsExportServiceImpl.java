package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.runner.common.bean.DocsExportFile;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ExportScopeEnum;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsInterfaceData;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docs.DocsExportRf;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsConfigService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsControllerDataService;
import io.gitee.xuchenoak.limejapidocs.runner.service.base.ApiDocsInterfaceDataService;
import io.gitee.xuchenoak.limejapidocs.runner.service.export.DocsExportControllerData;
import io.gitee.xuchenoak.limejapidocs.runner.service.export.DocsExportData;
import io.gitee.xuchenoak.limejapidocs.runner.service.export.DocsExportUtil;
import io.gitee.xuchenoak.limejapidocs.runner.service.export.MarkdownBuilder;
import io.gitee.xuchenoak.limejapidocs.runner.service.export.OpenApiBuilder;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsExportService;
import io.gitee.xuchenoak.limejapidocs.runner.util.IdUtils;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 文档导出业务实现
 *
 * @author xuchenoak
 **/
@Slf4j
@Service
public class DocsExportServiceImpl implements DocsExportService {

    @Resource
    private ApiDocsConfigService apiDocsConfigService;

    @Resource
    private ApiDocsControllerDataService apiDocsControllerDataService;

    @Resource
    private ApiDocsInterfaceDataService apiDocsInterfaceDataService;

    @Resource
    private OpenApiBuilder openApiBuilder;

    @Resource
    private MarkdownBuilder markdownBuilder;

    @Override
    public DocsExportFile exportOpenApi(DocsExportRf rf) {
        DocsExportData data = loadData(rf);
        String content = openApiBuilder.build(data);
        return new DocsExportFile(buildFileName(data, "-openapi.json"),
                "application/json;charset=UTF-8", content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public DocsExportFile exportMarkdown(DocsExportRf rf) {
        DocsExportData data = loadData(rf);
        String content = markdownBuilder.build(data);
        return new DocsExportFile(buildFileName(data, ".md"),
                "text/markdown;charset=UTF-8", content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加载导出数据
     *
     * @param rf 导出入参
     * @return 导出数据
     */
    private DocsExportData loadData(DocsExportRf rf) {
        Long docsConfigId = IdUtils.decryptIdOrExc(rf.getDocsConfigId());
        ApiDocsConfig docsConfig = apiDocsConfigService.getById(docsConfigId);
        if (docsConfig == null) {
            CusExc.e("文档配置不存在");
        }
        ExportScopeEnum scope = ExportScopeEnum.getEnum(rf.getScope());
        if (scope == null) {
            CusExc.e("不支持的导出范围");
        }

        List<ApiDocsControllerData> controllerList = apiDocsControllerDataService.list(q -> q
                .eq(ApiDocsControllerData::getDocsConfigId, docsConfigId)
                .eq(ApiDocsControllerData::getCreateTime, rf.getCreateTime())
                .orderByAsc(ApiDocsControllerData::getSort));
        if (ListUtils.isBlank(controllerList)) {
            CusExc.e("未找到对应生成版本的文档数据");
        }

        Map<String, List<InterfaceData>> interfaceMap = loadInterfaceMap(docsConfigId, rf.getCreateTime());
        List<DocsExportControllerData> exportControllerList = new ArrayList<>();
        String exportName = null;

        if (scope == ExportScopeEnum.PROJECT) {
            for (ApiDocsControllerData controller : controllerList) {
                if (controller == null || StrUtil.isBlank(controller.getComment())) {
                    continue;
                }
                List<InterfaceData> interfaceList = filterCommented(interfaceMap.get(controller.getControllerId()));
                if (interfaceList.isEmpty()) {
                    continue;
                }
                exportControllerList.add(new DocsExportControllerData(controller, interfaceList));
            }
        } else if (scope == ExportScopeEnum.CONTROLLER) {
            ApiDocsControllerData controller = findController(controllerList, rf.getControllerId());
            List<InterfaceData> interfaceList = filterCommented(interfaceMap.get(controller.getControllerId()));
            if (interfaceList.isEmpty()) {
                CusExc.e("该目录下暂无可导出的接口");
            }
            exportName = DocsExportUtil.controllerName(controller);
            exportControllerList.add(new DocsExportControllerData(controller, interfaceList));
        } else {
            ApiDocsControllerData controller = findController(controllerList, rf.getControllerId());
            InterfaceData interfaceData = findInterface(interfaceMap.get(controller.getControllerId()), rf.getInterfaceId());
            exportName = DocsExportUtil.interfaceName(interfaceData);
            List<InterfaceData> interfaceList = new ArrayList<>();
            interfaceList.add(interfaceData);
            exportControllerList.add(new DocsExportControllerData(controller, interfaceList));
        }

        if (exportControllerList.isEmpty()) {
            CusExc.e("未找到可导出的接口数据");
        }
        return new DocsExportData(docsConfig, rf.getCreateTime(), scope, exportName, exportControllerList);
    }

    /**
     * 加载controller下的接口数据
     *
     * @param docsConfigId 文档配置Id
     * @param createTime   生成时间
     * @return controllerId与接口数据的映射
     */
    private Map<String, List<InterfaceData>> loadInterfaceMap(Long docsConfigId, String createTime) {
        Map<String, List<InterfaceData>> interfaceMap = new HashMap<>();
        List<ApiDocsInterfaceData> interfaceDataList = apiDocsInterfaceDataService.list(q -> q
                .eq(ApiDocsInterfaceData::getDocsConfigId, docsConfigId)
                .eq(ApiDocsInterfaceData::getControllerCreateTime, createTime));
        if (ListUtils.isBlank(interfaceDataList)) {
            return interfaceMap;
        }
        for (ApiDocsInterfaceData interfaceData : interfaceDataList) {
            if (StrUtil.isBlank(interfaceData.getInterfaceDataList())) {
                continue;
            }
            try {
                interfaceMap.put(interfaceData.getControllerId(),
                        JSONUtil.toList(interfaceData.getInterfaceDataList(), InterfaceData.class));
            } catch (Exception e) {
                log.error("接口文档数据解析异常", e);
                CusExc.e("接口文档数据解析异常");
            }
        }
        return interfaceMap;
    }

    /**
     * 查找controller
     *
     * @param controllerList controller列表
     * @param controllerId   controller标识
     * @return controller数据
     */
    private ApiDocsControllerData findController(List<ApiDocsControllerData> controllerList, String controllerId) {
        if (StrUtil.isBlank(controllerId)) {
            CusExc.e("controller标识不能为空");
        }
        for (ApiDocsControllerData controller : controllerList) {
            if (controller != null && Objects.equals(controller.getControllerId(), controllerId)) {
                return controller;
            }
        }
        CusExc.e("controller不存在");
        return null;
    }

    /**
     * 查找接口
     *
     * @param interfaceList 接口列表
     * @param interfaceId   接口标识
     * @return 接口数据
     */
    private InterfaceData findInterface(List<InterfaceData> interfaceList, String interfaceId) {
        if (StrUtil.isBlank(interfaceId)) {
            CusExc.e("interface标识不能为空");
        }
        if (interfaceList != null) {
            for (InterfaceData interfaceData : interfaceList) {
                if (interfaceData != null && Objects.equals(interfaceData.getInterfaceId(), interfaceId)) {
                    return interfaceData;
                }
            }
        }
        CusExc.e("接口不存在");
        return null;
    }

    /**
     * 过滤有注释的接口
     *
     * @param interfaceList 接口列表
     * @return 有注释的接口列表
     */
    private List<InterfaceData> filterCommented(List<InterfaceData> interfaceList) {
        List<InterfaceData> result = new ArrayList<>();
        if (interfaceList == null) {
            return result;
        }
        for (InterfaceData interfaceData : interfaceList) {
            if (interfaceData != null && StrUtil.isNotBlank(interfaceData.getComment())) {
                result.add(interfaceData);
            }
        }
        return result;
    }

    /**
     * 构建导出文件名称
     *
     * @param data   导出数据
     * @param suffix 文件后缀
     * @return 文件名称
     */
    private String buildFileName(DocsExportData data, String suffix) {
        String fileName = DocsExportUtil.safeFileName(data.getDocsConfig().getDocsName());
        if (data.getScope() != ExportScopeEnum.PROJECT && StrUtil.isNotBlank(data.getExportName())) {
            fileName = fileName + "-" + DocsExportUtil.safeFileName(data.getExportName());
        }
        return fileName + suffix;
    }
}
