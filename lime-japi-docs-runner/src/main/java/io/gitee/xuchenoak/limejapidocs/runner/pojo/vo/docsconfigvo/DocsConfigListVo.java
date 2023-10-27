package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfigvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文档配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocsConfigListVo {

    /** Id */
    private Long id;

    /** 文档名称 */
    private String docsName;

    /** 文档版本号 */
    private String docsVersion;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

}
