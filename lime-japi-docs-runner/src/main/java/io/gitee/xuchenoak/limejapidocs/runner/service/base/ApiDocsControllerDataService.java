package io.gitee.xuchenoak.limejapidocs.runner.service.base;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.xuchenoak.limejapidocs.parser.bean.ControllerData;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.EntityBaseService;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsInterfaceData;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsInterfaceDataSearch;
import io.gitee.xuchenoak.limejapidocs.runner.mapper.ApiDocsControllerDataMapper;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档业务实现
 *
 * @author xuchenoak
 **/
@Service
public class ApiDocsControllerDataService extends EntityBaseService<ApiDocsControllerDataMapper, ApiDocsControllerData> {
    @Resource
    private ApiDocsInterfaceDataSearchService apiDocsInterfaceDataSearchService;

    @Resource
    private ApiDocsInterfaceDataService apiDocsInterfaceDataService;

    /**
     * 保存Controller接口数据
     *
     * @param docsConfigId       文档配置Id
     * @param controllerDataList 文档数据列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveControllerData(Long docsConfigId, List<ControllerData> controllerDataList) {
        if (ListUtil.isBlank(controllerDataList)) {
            return;
        }
        List<ApiDocsControllerData> apiDocsControllerDataList = new ArrayList<>();
        List<ApiDocsInterfaceData> apiDocsInterfaceDataList = new ArrayList<>();
        List<ApiDocsInterfaceDataSearch> apiDocsInterfaceDataSearchList = new ArrayList<>();
        for (ControllerData controllerData : controllerDataList) {
            String controllerId = SecureUtil.md5(controllerData.getControllerFullName());
            if (ListUtils.isNotBlank(controllerData.getInterfaceDataList())) {
                for (InterfaceData interfaceData : controllerData.getInterfaceDataList()) {
                    interfaceData.setControllerId(controllerId);
                    interfaceData.setInterfaceId(SecureUtil.md5(StrUtil.format(
                            "{}.{}.{}",
                            controllerId,
                            JSONUtil.toJsonStr(interfaceData.getRequestTypeList()),
                            JSONUtil.toJsonStr(interfaceData.getUriList()))));
                    apiDocsInterfaceDataSearchList.add(new ApiDocsInterfaceDataSearch(
                            docsConfigId,
                            controllerId,
                            controllerData.getComment(),
                            controllerData.getCreateTime(),
                            interfaceData.getInterfaceId(),
                            interfaceData.getComment(),
                            interfaceData.getUriList(),
                            interfaceData.getRequestTypeList()
                    ));
                }
            }
            apiDocsControllerDataList.add(new ApiDocsControllerData(
                    docsConfigId,
                    controllerId,
                    controllerData.getControllerFullName(),
                    controllerData.getComment(),
                    JSONUtil.toJsonStr(controllerData.getBaseUriList()),
                    controllerData.getSort(),
                    controllerData.getCreateTime()
            ));
            apiDocsInterfaceDataList.add(new ApiDocsInterfaceData(
                    docsConfigId,
                    controllerId,
                    controllerData.getComment(),
                    controllerData.getCreateTime(),
                    JSONUtil.toJsonStr(controllerData.getInterfaceDataList())
            ));
        }
        addBatchIfCount(apiDocsControllerDataList);
        apiDocsInterfaceDataService.addBatchIfCount(apiDocsInterfaceDataList);
        apiDocsInterfaceDataSearchService.addBatchIfCount(apiDocsInterfaceDataSearchList);
    }
}
