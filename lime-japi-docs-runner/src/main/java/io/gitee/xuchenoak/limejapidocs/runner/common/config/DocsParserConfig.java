package io.gitee.xuchenoak.limejapidocs.runner.common.config;

import io.gitee.xuchenoak.limejapidocs.parser.util.StringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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

    /** 标语 */
    private String slogan;

    /** 文档管理秘钥 */
    private String docsConfigKey;

    /** 文档查看秘钥：不设置则所有人都可访问所有文档，若设置了需通过该秘钥才可访问所有文档（具体实现见文档配置列表查询接口） */
    private String docsViewKey;

    public boolean checkDocsConfigKey(String docsConfigKey) {
        if (StringUtil.isBlank(this.docsConfigKey)) {
            return true;
        }
        if (StringUtil.isBlank(docsConfigKey)) {
            return false;
        }
        return this.docsConfigKey.equals(docsConfigKey);
    }

    public boolean checkDocsConfigKey(HttpServletRequest request) {
        String docsConfigKey = request.getHeader("Dck");
        return checkDocsConfigKey(docsConfigKey);
    }

    public boolean checkDocsViewKey(String docsViewKey) {
        if (StringUtil.isBlank(this.docsViewKey)) {
            return true;
        }
        if (StringUtil.isBlank(docsViewKey)) {
            return false;
        }
        return this.docsViewKey.equals(docsViewKey);
    }
}
