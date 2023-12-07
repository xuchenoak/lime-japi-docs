package io.gitee.xuchenoak.limejapidocs.runner.pojo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 全局配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonConfig implements Serializable {

    /** 文档中心名称 */
    private String docsCenterName;

    /** 标语 */
    private String slogan;

    /** 文档管理秘钥 */
    private String docsConfigKey;

    /** 文档查看秘钥 */
    private String docsViewKey;

}
