package io.gitee.xuchenoak.limejapidocs.runner.service.inter;

import io.gitee.xuchenoak.limejapidocs.runner.common.bean.DocsExportFile;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docs.DocsExportRf;

/**
 * 文档导出业务接口
 *
 * @author xuchenoak
 **/
public interface DocsExportService {

    /**
     * 导出OpenAPI文档
     *
     * @param rf 导出入参
     * @return 导出文件
     */
    DocsExportFile exportOpenApi(DocsExportRf rf);

    /**
     * 导出Markdown文档
     *
     * @param rf 导出入参
     * @return 导出文件
     */
    DocsExportFile exportMarkdown(DocsExportRf rf);
}
