package io.gitee.xuchenoak.limejapidocs.runner.pojo.vo.sysconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统配置
 *
 * @author xuchenoak
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfigVo implements Serializable {

    /** 文档中心名称 */
    private String sysName;

    /** 系统LOGO */
    private String logoUrl;

    /** 系统标语 */
    private String sysSlogan;

    /** 文档中心邀请码 */
    private String docsViewKey;

    /** 管理员账号 */
    private String account;

    /** 管理员密码 */
    private String password;

}
