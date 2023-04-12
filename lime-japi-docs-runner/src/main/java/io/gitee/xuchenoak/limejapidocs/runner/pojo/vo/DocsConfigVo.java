package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口文档配置输出对象
 *
 * @author: xuchenoak
 * @create: 2022-06-06 8:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsConfigVo {

    /** 文档名称 */
    private String docName;

    /** 文档版本 */
    private String docVersion;
}
