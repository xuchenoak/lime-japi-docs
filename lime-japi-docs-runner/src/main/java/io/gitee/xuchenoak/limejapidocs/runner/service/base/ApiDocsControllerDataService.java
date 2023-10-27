package io.gitee.xuchenoak.limejapidocs.runner.service.base;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.gitee.xuchenoak.limejapidocs.parser.bean.ControllerData;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.mybatisplus.EntityBaseService;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import io.gitee.xuchenoak.limejapidocs.runner.mapper.ApiDocsControllerDataMapper;
import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档业务实现
 *
 * @author xuchenoak
 **/
@Service
public class ApiDocsControllerDataService extends EntityBaseService<ApiDocsControllerDataMapper, ApiDocsControllerData> {

    /**
     * 获取所有接口数据
     * @param createTime 生成时间
     * @return
     */
    public List<ControllerData> getControllerDataList(Long docsConfigId, String createTime) {
        List<ControllerData> list = new ArrayList<>();
        if (createTime == null) {
            return list;
        }
        List<ApiDocsControllerData> apiDocsControllerDataList = list(
                new LambdaQueryWrapper<ApiDocsControllerData>()
                        .eq(ApiDocsControllerData::getDocsConfigId, docsConfigId)
                        .eq(ApiDocsControllerData::getCreateTime, createTime)
                        .orderByAsc(ApiDocsControllerData::getSort)
        );
        if (ListUtils.isBlank(apiDocsControllerDataList)) {
            return list;
        }
        for (ApiDocsControllerData apiDocsControllerData : apiDocsControllerDataList) {
            list.add(toControllerData(apiDocsControllerData));
        }
        return list;
    }

    /**
     * 获取单个Controller接口数据
     * @param controllerId
     * @return
     */
    public ControllerData getControllerData(String createTime, String controllerId) {
        return toControllerData(getOne(
                new LambdaQueryWrapper<ApiDocsControllerData>()
                        .eq(ApiDocsControllerData::getCreateTime, createTime)
                        .eq(ApiDocsControllerData::getControllerId, controllerId)
        ));
    }

    /**
     * 保存Controller接口数据
     * @param docsConfigId 文档配置Id
     * @param controllerDataList
     */
    public void saveControllerData(Long docsConfigId, List<ControllerData> controllerDataList) {
        if (ListUtil.isBlank(controllerDataList)) {
            return;
        }
        List<ApiDocsControllerData> apiDocsControllerDataList = new ArrayList<>();
        for (ControllerData controllerData : controllerDataList) {
            apiDocsControllerDataList.add(toApiDocsControllerData(docsConfigId, controllerData));
        }
        saveBatch(apiDocsControllerDataList);
    }


    /**
     * 对象转换
     * @param data
     * @return
     */
    private ControllerData toControllerData(ApiDocsControllerData data) {
        if (data == null) {
            return null;
        }
        List<InterfaceData> interfaceDataList = JSONUtil.toList(data.getInterfaceDataList(), InterfaceData.class);
        if (ListUtil.isBlank(interfaceDataList)) {
            return null;
        }
        return new ControllerData(
                data.getControllerId(),
                data.getControllerFullName(),
                data.getComment(),
                JSONUtil.toList(data.getBaseUriList(), String.class),
                interfaceDataList,
                data.getSort(),
                data.getCreateTime()
        );
    }

    /**
     * 对象转换
     * @param docsConfigId
     * @param data
     * @return
     */
    private ApiDocsControllerData toApiDocsControllerData(Long docsConfigId, ControllerData data) {
        return new ApiDocsControllerData(
                0L,
                docsConfigId,
                data.getControllerId(),
                data.getControllerFullName(),
                data.getComment(),
                JSONUtil.toJsonStr(data.getBaseUriList()),
                JSONUtil.toJsonStr(data.getInterfaceDataList()),
                data.getSort(),
                data.getCreateTime()
        );
    }
}
