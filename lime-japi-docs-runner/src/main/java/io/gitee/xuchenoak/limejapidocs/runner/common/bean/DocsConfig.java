package io.gitee.xuchenoak.limejapidocs.runner.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文档配置类
 *
 * @author: xuchenoak
 * @create: 2022/06/04 19:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsConfig {

    /** 文档名称 */
    private String docName = "XX项目接口文档";

    /** 文档版本 */
    private String docVersion = "V1.0";

}
