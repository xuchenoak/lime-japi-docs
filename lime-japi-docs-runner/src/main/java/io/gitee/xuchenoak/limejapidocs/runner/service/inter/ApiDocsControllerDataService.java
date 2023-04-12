package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.parser.bean.ControllerData;
import java.util.List;

/**
 * 接口文档业务接口
 *
 * @author xuchenoak
 **/
public interface ApiDocsControllerDataService {

    /**
     * 获取生成时间集
     * @return
     */
    List<String> getCreateTimeList();

    /**
     * 获取所有接口数据
     * @param createTime 生成时间
     * @return
     */
    List<ControllerData> getControllerDataList(String createTime);

    /**
     * 获取单个Controller接口数据
     * @param controllerId
     * @return
     */
    ControllerData getControllerData(String createTime, String controllerId);

    /**
     * 保存Controller接口数据
     * @param controllerDataList
     */
    void saveControllerData(List<ControllerData> controllerDataList);

}
