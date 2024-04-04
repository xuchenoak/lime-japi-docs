package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.docsconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共配置输出
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonConfigVo {

    /** 系统名称 */
    private String sysName;

    /** 系统标语 */
    private String sysSlogan;

    /** logo地址 */
    private String logoUrl;

    /** 是否已登录 */
    private Boolean hasLogin;

    /** 是否已初始化 */
    private Boolean hasInit;

}
