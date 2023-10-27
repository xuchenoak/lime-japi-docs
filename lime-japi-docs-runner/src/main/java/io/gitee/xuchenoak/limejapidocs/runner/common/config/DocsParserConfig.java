package io.gitee.xuchenoak.limejapidocs.runner.common.config;

import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    /** 文档管理秘钥 */
    private String docsConfigKey;

    public boolean checkDocsConfigKey(String docsConfigKey) {
        if (StringUtil.isBlank(this.docsConfigKey)) {
            return true;
        }
        if (StringUtil.isBlank(docsConfigKey)) {
            return false;
        }
        return this.docsConfigKey.equals(docsConfigKey);
    }
}
