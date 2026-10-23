package io.gitee.xuchenoak.limejapidocs.runner.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文档导出文件
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsExportFile {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件内容
     */
    private byte[] content;
}
