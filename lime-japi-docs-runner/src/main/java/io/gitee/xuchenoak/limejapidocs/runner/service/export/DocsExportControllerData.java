package io.gitee.xuchenoak.limejapidocs.runner.service.export;

import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsControllerData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文档导出controller数据
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsExportControllerData {

    /**
     * controller数据
     */
    private ApiDocsControllerData controller;

    /**
     * controller下的接口数据
     */
    private List<InterfaceData> interfaceList;
}
