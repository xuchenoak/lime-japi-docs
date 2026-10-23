package io.gitee.xuchenoak.limejapidocs.runner.controller;

import io.gitee.xuchenoak.limejapidocs.runner.common.bean.DocsExportFile;
import io.gitee.xuchenoak.limejapidocs.runner.pojo.rf.docs.DocsExportRf;
import io.gitee.xuchenoak.limejapidocs.runner.service.inter.DocsExportService;
import jakarta.annotation.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * 文档导出业务接口
 *
 * @author xuchenoak
 **/
@Validated
@RestController
@RequestMapping("/lime_japi_docs/api/docs/export")
public class DocsExportController {

    @Resource
    private DocsExportService docsExportService;

    /**
     * 导出OpenAPI文档
     *
     * @param rf 导出入参
     * @return OpenAPI文件
     */
    @GetMapping("/openapi")
    public ResponseEntity<byte[]> openApi(@Validated DocsExportRf rf) {
        return buildResponse(docsExportService.exportOpenApi(rf), rf.isDownload());
    }

    /**
     * 导出Markdown文档
     *
     * @param rf 导出入参
     * @return Markdown文件
     */
    @GetMapping("/markdown")
    public ResponseEntity<byte[]> markdown(@Validated DocsExportRf rf) {
        return buildResponse(docsExportService.exportMarkdown(rf), rf.isDownload());
    }

    /**
     * 构建文件响应
     *
     * @param exportFile 导出文件
     * @param download   是否下载
     * @return 文件响应
     */
    private ResponseEntity<byte[]> buildResponse(DocsExportFile exportFile, boolean download) {
        ContentDisposition contentDisposition = download
                ? ContentDisposition.attachment().filename(exportFile.getFileName(), StandardCharsets.UTF_8).build()
                : ContentDisposition.inline().filename(exportFile.getFileName(), StandardCharsets.UTF_8).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .contentType(MediaType.parseMediaType(exportFile.getContentType()))
                .body(exportFile.getContent());
    }
}
