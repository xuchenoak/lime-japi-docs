package io.gitee.xuchenoak.limejapidocs.runner.service.export;

import io.gitee.xuchenoak.limejapidocs.parser.bean.InterfaceData;
import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ExportScopeEnum;
import io.gitee.xuchenoak.limejapidocs.runner.domain.ApiDocsConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文档导出数据
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsExportData {

    /**
     * 文档配置
     */
    private ApiDocsConfig docsConfig;

    /**
     * 文档生成时间
     */
    private String createTime;

    /**
     * 导出范围
     */
    private ExportScopeEnum scope;

    /**
     * 当前导出的controller或接口名称
     */
    private String exportName;

    /**
     * 待导出的controller数据
     */
    private List<DocsExportControllerData> controllerList;
}
