package io.gitee.xuchenoak.limejapidocs.runner.config;

import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * 文档配置
 *
 * @author: xuchenoak
 * @create: 2022-12-02 16:55
 **/
@Data
@Component
@ConfigurationProperties(prefix = "docs-parser")
public class DocsParserConfig {

    /** 文档名称 */
    private String docsName;

    /** 文档版本 */
    private String version;

    /** 启动时是否解析 */
    private Boolean sysStartParse;

    /** java文件所在目录绝对路径（必须写到java目录） */
    private Set<String> javaFilePaths;

    /** 仅扫描解析该包集合下的controller类（必须位于javaFilePaths下，若不配置默认扫描javaFilePaths下所有） */
    private Set<String> filterPackages;

    /** 仅扫描的controller类名集（非类全名） */
    private Set<String> filterClassNames;

    /** 需要排除的controller类名集（非类全名） */
    private Set<String> ignoreClassNames;

    /** 执行解析秘钥 */
    private String apiRunKey;

    public Boolean getSysStartParse() {
        return Optional.ofNullable(sysStartParse).orElse(false);
    }

    public boolean checkedApiRun(String apiRunKey) {
        if (StringUtil.isBlank(this.apiRunKey)) {
            return true;
        }
        if (StringUtil.isBlank(apiRunKey)) {
            return false;
        }
        return this.apiRunKey.equals(apiRunKey);
    }
}
