package io.gitee.xuchenoak.limejapidocs.runner.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.xuchenoak.limejapidocs.parser.bean.ControllerData;
import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.parser.util.ListUtil;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import io.gitee.xuchenoak.limejapidocs.runner.mapper.ApiDocsControllerDataMapper;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.ApiDocsControllerDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档业务实现
 *
 * @author xuchenoak
 **/
@Service
public class ApiDocsControllerDataServiceImpl extends ServiceImpl<ApiDocsControllerDataMapper, ApiDocsControllerData> implements ApiDocsControllerDataService {

    /**
     * 获取生成时间集
     * @return
     */
    @Override
    public List<String> getCreateTimeList() {
        List<String> dateList = getBaseMapper().listCreateTime();
        if (ListUtil.isBlank(dateList)) {
            return new ArrayList<>();
        }
        return dateList;
    }

    /**
     * 获取所有接口数据
     * @param createTime 生成时间
     * @return
     */
    @Override
    public List<ControllerData> getControllerDataList(String createTime) {
        List<ControllerData> list = new ArrayList<>();
        if (createTime == null) {
            return list;
        }
        List<ApiDocsControllerData> apiDocsControllerDataList = list(
                new LambdaQueryWrapper<ApiDocsControllerData>()
                        .eq(ApiDocsControllerData::getCreateTime, createTime)
                        .orderByAsc(ApiDocsControllerData::getSort)
        );
        if (ListUtil.isBlank(apiDocsControllerDataList)) {
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
    @Override
    public ControllerData getControllerData(String createTime, String controllerId) {
        return toControllerData(getOne(
                new LambdaQueryWrapper<ApiDocsControllerData>()
                        .eq(ApiDocsControllerData::getCreateTime, createTime)
                        .eq(ApiDocsControllerData::getControllerId, controllerId)
        ));
    }

    /**
     * 保存Controller接口数据
     * @param controllerDataList
     */
    @Override
    public void saveControllerData(List<ControllerData> controllerDataList) {
        if (ListUtil.isBlank(controllerDataList)) {
            return;
        }
        List<ApiDocsControllerData> apiDocsControllerDataList = new ArrayList<>();
        for (ControllerData controllerData : controllerDataList) {
            apiDocsControllerDataList.add(toApiDocsControllerData(controllerData));
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
     * @param data
     * @return
     */
    private ApiDocsControllerData toApiDocsControllerData(ControllerData data) {
        return new ApiDocsControllerData(
                0L,
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
