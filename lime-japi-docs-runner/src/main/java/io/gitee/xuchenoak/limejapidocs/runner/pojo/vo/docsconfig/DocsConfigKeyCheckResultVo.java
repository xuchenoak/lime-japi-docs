package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文档配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsConfigKeyCheckResultVo {

    /** 文档管理秘钥 */
    private String docsConfigKey;

    /** 秘钥是否正确 */
    private Boolean checkResult;

}
