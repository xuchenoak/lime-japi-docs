package io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docs;

import io.gitee.xuchenoak.limejapidocs.runner.common.enums.ExportScopeEnum;
import io.gitee.xuchenoak.limejapidocs.runner.common.validatron.EnumValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 文档导出入参
 *
 * @author xuchenoak
 **/
@Data
public class DocsExportRf {

    /**
     * 文档配置Id
     */
    @NotBlank(message = "文档配置Id不能为空")
    private String docsConfigId;

    /**
     * 文档生成时间
     */
    @NotBlank(message = "生成时间不能为空")
    private String createTime;

    /**
     * 导出范围
     */
    @NotBlank(message = "导出范围不能为空")
    @EnumValid(enumClass = ExportScopeEnum.class)
    private String scope;

    /**
     * controller标识（导出单个controller或单个接口时必填）
     */
    private String controllerId;

    /**
     * 接口标识（导出单个接口时必填）
     */
    private String interfaceId;

    /**
     * 是否下载文件：false-浏览器预览，true-下载
     */
    private boolean download;
}
